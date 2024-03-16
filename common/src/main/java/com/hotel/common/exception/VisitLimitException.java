package com.hotel.common.exception;

/**
 * 访问过于频繁异常
 *
 * @author: 郭晨旭
 * @create: 2024-01-23 16:48
 * @version: 1.0
 */
public class VisitLimitException extends RuntimeException {

    private String message;

    public VisitLimitException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
