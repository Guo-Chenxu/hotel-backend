package com.hotel.timer.thread;

import java.util.Date;

/**
 * 模拟时间的线程
 */
public class TimeThread extends Thread {
    private static final TimeThread timeThread = new TimeThread();

    boolean stopped = false;

    Date now;

    // 1秒对应speed毫秒
    long speed;

    private TimeThread() {
    }

    public static TimeThread getInstance() {
        return timeThread;
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
            //每过50ms跳动一下时间
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            now = new Date(now.getTime() + speed);
        }
    }
}