package com.hotel.server.exception;

/**
 * 指定参数不存在异常
 *
 * @author: 郭晨旭
 * @create: 2024-01-23 16:48
 * @version: 1.0
 */
public class NotFoundException extends RuntimeException {

    private String message;

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
