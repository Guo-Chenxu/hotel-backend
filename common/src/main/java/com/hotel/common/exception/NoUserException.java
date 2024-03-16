package com.hotel.common.exception;

/**
 * 用户不存在异常
 *
 * @author: 郭晨旭
 * @create: 2024-01-23 16:48
 * @version: 1.0
 */
public class NoUserException extends RuntimeException {

    private String message;

    public NoUserException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
