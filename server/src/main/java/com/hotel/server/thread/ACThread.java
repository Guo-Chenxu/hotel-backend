package com.hotel.server.thread;

import com.alibaba.fastjson2.JSON;
import com.hotel.common.constants.ACStatus;
import com.hotel.common.dto.response.ACStatusResp;
import com.hotel.common.entity.ACRequest;
import com.hotel.common.entity.CustomerAC;
import com.hotel.common.service.server.BillService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.config.IndoorTemperatureConfig;
import com.hotel.server.service.ACScheduleService;
import com.hotel.server.ws.WebSocketServer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private Date requestTime; // 请求服务时间
    private long lastTime; // 空调上一次计费温度
    private long timeOutTime; // 时间片到时的时间

    private Integer status; // 空调状态, 0 关闭， 1 低档， 2 中档， 3 高档 4 等待 5 回温
    private Integer lastStatus; // 上一阶段空调状态

    private Double temperature; // 当前温度
    private Double targetTemperature; // 目标温度
    private Double changeTemperature; // 每分钟变化温度
    private Double indoorTemperature; // 室内初始温度
    private IndoorTemperatureConfig indoorTemperatureConfig; // 室内温度参数

    private Boolean isRunning; // 控制线程是否继续运行
    private Boolean recover; // 控制是否在回归室温

    private String price; // 价格
    private String lastPrice; // 上一次价格
    // 当前费用就实时计算，每次turnOff清0
    // 累计费用在当前费用清0时增加当前费用，初始化时从数据库中计算累计费用
    private String currentPrice; // 当前费用
    private String totalPrice; // 累计费用

    private TimerService timerService;
    private BillService billService;
    private ACScheduleService acScheduleService;

    private WebSocketServer webSocketServer; //websocket 连接

    @Override
    public void run() {
        isRunning = true;
        recover = true;
        lastTime = timerService.getTime().getTime();
        long now = timerService.getTime().getTime();
        long dur = (now - lastTime) / 1000;
        while (isRunning) {
            while (dur < 1) {
                now = timerService.getTime().getTime();
                dur = (now - lastTime) / 1000;
            }
            if (ACStatus.OFF.equals(status) || ACStatus.WAITING.equals(status)
                    || ACStatus.RETURNING.equals(status)) {
                if (ACStatus.RETURNING.equals(status)
                        && compareTemperature(temperature, targetTemperature, 1) != 0) {
                    this.change(targetTemperature, changeTemperature, lastStatus, lastPrice);
                }
                if (compareTemperature(temperature, indoorTemperature, 0.1) > 0) {
                    temperature -= indoorTemperatureConfig.getRecoverChangeTemperature() / 60.0 * dur;
                } else if (compareTemperature(temperature, indoorTemperature, 0.1) < 0) {
                    temperature += indoorTemperatureConfig.getRecoverChangeTemperature() / 60.0 * dur;
                }
            } else {
                // 时间片未到达或者没有请求
                if (now < timeOutTime || acScheduleService.isRequestEmpty()) {
                    if (acScheduleService.isRequestEmpty() && now >= timeOutTime) {
                        updateTimeOutTime(timeOutTime);
                    }

                    if (compareTemperature(temperature, targetTemperature, 0.1) > 0) {
                        temperature -= changeTemperature / 60.0 * dur;
                        changeCurrentPrice(dur);
                    } else if (compareTemperature(temperature, targetTemperature, 0.1) < 0) {
                        temperature += changeTemperature / 60.0 * dur;
                        changeCurrentPrice(dur);
                    } else {
                        // 温度相差1度则关闭空调
                        this.lastStatus = this.status;
                        this.lastPrice = this.price;
                        this.turnOff();
                        this.status = ACStatus.RETURNING;
                    }
                } else {
                    // 时间片到时, 重新调度
                    ACRequest acRequest = this.turnOff();
                    acScheduleService.addOne(acRequest);
                }
            }
            lastTime = timerService.getTime().getTime();
            dur = 0;
            webSocketServer.sendOneMessage(userId, JSON.toJSONString(this.getACStatus()));
//            mySleep(1000);
        }
    }

    /**
     * 获取空调的当前状态
     */
    private ACStatusResp getACStatus() {
        ACStatusResp resp = ACStatusResp.builder().temperature(this.temperature)
                .status(this.status)
                .currentPrice(this.currentPrice)
                .totalPrice(this.totalPrice).build();
        if (!ACStatus.OFF.equals(resp.getStatus())) {
            resp.setPrice(this.price);
            resp.setChangeTemp(this.changeTemperature);
            resp.setTargetTemp(this.targetTemperature);
        }
        return resp;
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
     * 比较温度
     */
    private int compareTemperature(double a, double b, double precision) {
        if (a - b > precision) {
            return 1;
        } else if (b - a > precision) {
            return -1;
        }
        return 0;
    }

    /**
     * 关闭
     */
    public ACRequest turnOff() {
        if (checkACStatus()) {
            return null;
        }

        changeTotalPrice();
        acScheduleService.removeOne(userId);

        return storeACRequest();
    }

    /**
     * 关闭
     */
    public ACRequest turnOffInSchedule() {
        if (checkACStatus()) {
            return null;
        }

        changeTotalPrice();
        return storeACRequest();
    }

    /**
     * 当前费用增加
     */
    private void changeCurrentPrice(long _dur) {
        currentPrice = new BigDecimal(currentPrice)
                .add(new BigDecimal(price)
                        .divide(new BigDecimal(60), 5, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(_dur))
                ).toString();
    }

    /**
     * 当前费用清0，实时费用增加
     */
    private void changeTotalPrice() {
        totalPrice = new BigDecimal(totalPrice).add(new BigDecimal(currentPrice)).toString();
        currentPrice = "0";
    }

    /**
     * 校验空调状态
     * 如果当前空调本身就没有开启, 则不需要再进行调度
     * 从队列中删除后直接返回即可
     */
    private boolean checkACStatus() {
        if (Objects.equals(status, ACStatus.OFF) || Objects.equals(status, ACStatus.WAITING)
                || Objects.equals(status, ACStatus.RETURNING)) {
            acScheduleService.removeOne(userId);
            status = ACStatus.OFF;
            return true;
        }
        return false;
    }

    private ACRequest storeACRequest() {
        endTime = timerService.getTime();
//        int duration = (int) Math.ceil((endTime.getTime() - startTime.getTime()) * 1.0 / 1000 / 60);
        BigDecimal duration = BigDecimal.valueOf((endTime.getTime() - startTime.getTime()))
                .divide(BigDecimal.valueOf(1.0 * 1000 * 60), 3, RoundingMode.HALF_UP);
        if (duration.compareTo(BigDecimal.ZERO) > 0) {
            String totalPrice = new BigDecimal(price).multiply(duration).toString();

            CustomerAC customerAC = CustomerAC.builder().customerId(userId).price(price).status(status)
                    .changeTemperature(changeTemperature).duration(duration.toString()).totalPrice(totalPrice)
                    .createAt(startTime).requestTime(requestTime).endTime(endTime).build();
            billService.saveACBill(customerAC);
            log.info("空调关闭且持续时间大于0, 服务信息入库: {}", customerAC);
        }

        ACRequest acRequest = ACRequest.builder().userId(userId).startTime(timerService.getTime())
                .targetTemperature(targetTemperature).changeTemperature(changeTemperature)
                .status(status).price(price).build();
        log.info("空调关闭, 此次请求信息: {}", acRequest);

        status = ACStatus.OFF;
        lastPrice = price;
        price = "0";

        return acRequest;
    }

    /**
     * 启动
     */
    public void turnOn(Double _targetTemperature, Double _changeTemperature, Integer _status, String _price) {
        startTime = timerService.getTime();
        lastTime = timerService.getTime().getTime();
        updateTimeOutTime(startTime.getTime());
        targetTemperature = _targetTemperature;
        changeTemperature = _changeTemperature;
        status = _status;
        price = _price;
    }

    /**
     * 更新时间片到时时间
     */
    private void updateTimeOutTime(long time) {
        this.timeOutTime = time + indoorTemperatureConfig.getTimeSlice() * 60 * 1000;
        log.info("用户 {} 空调开启, 服务开始时间时间: {}, 更新时间片到时时间: {}", userId, this.startTime, this.timeOutTime);
    }


    /**
     * 调温调风
     * 结束之前的运行, 开启一个新的请求
     */
    public void change(Double _targetTemperature, Double _changeTemperature, Integer _status, String _price) {
        // 先关闭 再放入等待队列
        log.info("用户 {} 空调改变参数: target={}, change={}, status={}, price={}",
                userId, _targetTemperature, _changeTemperature, _status, _price);
        // 调温不会调度
        if (status != null && price != null) {
            if (status.equals(_status) && price.equals(_price)) {
                targetTemperature = _targetTemperature;
                return;
            }
        }

        turnOff();
        this.requestTime = timerService.getTime();
        this.targetTemperature = _targetTemperature;
        this.changeTemperature = _changeTemperature;
        this.price = _price;
        this.status = ACStatus.WAITING;
        ACRequest acRequest = ACRequest.builder().userId(userId).startTime(timerService.getTime())
                .targetTemperature(_targetTemperature).changeTemperature(_changeTemperature)
                .status(_status).price(_price).build();
        acScheduleService.addOne(acRequest);
    }

    /**
     * 设置当前线程的各项参数
     */
    public void setReq(ACRequest acRequest) {
        this.requestTime = timerService.getTime();
        this.targetTemperature = acRequest.getTargetTemperature();
        this.changeTemperature = acRequest.getChangeTemperature();
        this.price = acRequest.getPrice();
        this.status = acRequest.getStatus();
    }
}
