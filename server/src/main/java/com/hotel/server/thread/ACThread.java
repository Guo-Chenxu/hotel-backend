package com.hotel.server.thread;

import com.hotel.common.service.server.CoolService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.config.IndoorTemperatureConfig;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.util.Date;

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

    // todo 价格 空调档位 时间 要传给财务

    private PrintWriter writer; // sse输出流

    private TimerService timerService;
//    todo 财务服务

    @Override
    public void run() {
        isRunning = true;
        recover = true;
        while (isRunning) {
            while (recover) {
                if (temperature - indoorTemperatureConfig.getIndoorTemperature() > 0.0000001) {
                    temperature -= indoorTemperatureConfig.getRecoverChangeTemperature() / 60.0;
                } else if (indoorTemperatureConfig.getIndoorTemperature() - temperature > 0.0000001) {
                    temperature += indoorTemperatureConfig.getRecoverChangeTemperature() / 60.0;
                }
                writer.write("data: " + temperature + "\n\n");
                writer.flush();
                mySleep(1000);
            }
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

    // todo 调温 调风 启动(时间片到达以后将自己弄一个新请求放入优先队列) 关机
}
