package com.hotel.server.thread;

import com.hotel.common.constants.ACStatus;
import com.hotel.common.entity.ACRequest;
import com.hotel.common.entity.CustomerAC;
import com.hotel.common.service.server.BillService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.config.IndoorTemperatureConfig;
import com.hotel.server.service.ACScheduleService;
import com.hotel.server.service.impl.ACScheduleServiceImpl;
import com.hotel.server.ws.WebSocketServer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 空调线程
 *
 * @author: 郭晨旭
 * @create: 2024-03-27 11:12
 * @version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ACThread extends Thread {
    private String userId; // 用户id

    private Date startTime; // 申请空调启动时间
    private Date endTime; // 申请空调停止时间 (时间片请求时自动增加)

    private Integer status; // 空调状态, 0 关闭， 1 低档， 2 中档， 3 高档

    private Double temperature; // 当前温度
    private Double targetTemperature; // 目标温度
    private Double changeTemperature; // 每分钟变化温度
    private IndoorTemperatureConfig indoorTemperatureConfig; // 室内温度参数

    private Boolean isRunning; // 控制线程是否继续运行
    private Boolean recover; // 控制是否在回归室温

    private String price; // 价格

    private TimerService timerService;
    private BillService billService;
    private ACScheduleService acScheduleService;

    private WebSocketServer webSocketServer; //websocket 连接

    @Override
    public void run() {
        isRunning = true;
        recover = true;
        while (isRunning) {
            if (status == 0) {
                if (temperature.compareTo(indoorTemperatureConfig.getIndoorTemperature()) > 0) {
                    temperature -= indoorTemperatureConfig.getRecoverChangeTemperature() / 60.0;
                } else if (temperature.compareTo(indoorTemperatureConfig.getIndoorTemperature()) < 0) {
                    temperature += indoorTemperatureConfig.getRecoverChangeTemperature() / 60.0;
                }
            } else {
                if (temperature.compareTo(targetTemperature) > 0) {
                    temperature -= changeTemperature / 60.0;
                } else if (temperature.compareTo(targetTemperature) < 0) {
                    targetTemperature += changeTemperature / 60.0;
                } else {
                    this.turnOff();
                }
            }
            webSocketServer.sendOneMessage(userId, String.valueOf(temperature));
            mySleep(1000);
        }
    }

    /**
     * 线程休眠
     */
    private void mySleep(long millis) {
        long start = timerService.getTime().getTime();
        while (timerService.getTime().getTime() - start < millis) {
        }
    }

    /**
     * 关闭
     */
    public ACRequest turnOff() {
        if (Objects.equals(status, ACStatus.OFF)) {
            return null;
        }
        acScheduleService.removeOne(userId);

        endTime = timerService.getTime();
        int duration = (int) Math.ceil((endTime.getTime() - startTime.getTime()) * 1.0 / 1000 / 60);
        String totalPrice = new BigDecimal(price).multiply(new BigDecimal(duration)).toString();

        CustomerAC customerAC = CustomerAC.builder().customerId(userId).price(price).status(status)
                .changeTemperature(changeTemperature).duration(duration).totalPrice(totalPrice).build();
        billService.saveACBill(customerAC);

        ACRequest acRequest = ACRequest.builder().userId(userId).startTime(startTime)
                .targetTemperature(targetTemperature).changeTemperature(changeTemperature)
                .status(status).price(price).build();

        status = ACStatus.OFF;
        price = "0";

        return acRequest;
    }

    /**
     * 启动
     */
    public void turnOn(Double _targetTemperature, Double _changeTemperature, Integer _status, String _price) {
        startTime = timerService.getTime();
        targetTemperature = _targetTemperature;
        changeTemperature = _changeTemperature;
        status = _status;
        price = _price;
    }


    /**
     * 调温调风
     * 结束之前的运行, 开启一个新的请求
     */
    public void change(Double _targetTemperature, Double _changeTemperature, Integer _status, String _price) {
        // 先关闭 再放入等待队列
        log.info("空调改变参数: target={}, change={}, status={}, price={}",
                _targetTemperature, _changeTemperature, _status, _price);
        if (targetTemperature != null && status != null && changeTemperature != null && price != null) {
            if (targetTemperature.equals(_targetTemperature) && status.equals(_status)
                    && changeTemperature.equals(_changeTemperature) && price.equals(_price)) {
                return;
            }
        }
        turnOff();
        ACRequest acRequest = ACRequest.builder().userId(userId).startTime(timerService.getTime())
                .targetTemperature(_targetTemperature).changeTemperature(_changeTemperature)
                .status(_status).price(_price).build();
        acScheduleService.addOne(acRequest);
    }
}
