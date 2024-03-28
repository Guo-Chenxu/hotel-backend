package com.hotel.server.exception;

/**
 * 用户非法操作
 *
 * @author: 郭晨旭
 * @create: 2024-01-27 17:01
 * @version: 1.0
 */
public class IllegalException extends RuntimeException {

    private String message;

    public IllegalException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
