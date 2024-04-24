package com.hotel.timer.thread;

import cn.hutool.core.date.DateUtil;
import com.hotel.common.service.server.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 模拟时间的线程
 */
@Slf4j
public class TimeThread extends Thread {
    private static final TimeThread timeThread = new TimeThread();

    public String channel; //频道channel

    boolean stopped = false;

    Date now;

    // 1秒对应speed秒
    long speed;

    private StringRedisTemplate stringRedisTemplate;

    private TimeThread() {
    }

    public static TimeThread getInstance() {
        return timeThread;
    }

    public void setChannel(String _channel) {
        this.channel = _channel;
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Date getNow() {
        return now;
    }

    public long getSpeed() {
        return speed;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public void setStop() {
        this.stopped = true;
    }

    public void finish() {
        this.stopped = true;
    }

    @Override
    public void run() {
        while (!stopped) {
            //每过100ms跳动一下时间
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            now = new Date(now.getTime() + speed * 1000);

            LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
            if (localDateTime.getMinute() == 0 && localDateTime.getSecond() == 0) {
                //  redis发布订阅模式 每小时同步房间温度到数据库中
                String t = DateUtil.format(now, "yyyy-MM-dd HH:mm:ss");
                log.info("此时是: {}, 发布房间温度同步消息", t);
                stringRedisTemplate.convertAndSend(channel, t);
            }
        }
    }
}