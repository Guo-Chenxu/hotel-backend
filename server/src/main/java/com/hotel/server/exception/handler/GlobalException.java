package com.hotel.server.exception.handler;


import com.hotel.common.constants.HttpCode;
import com.hotel.common.dto.R;
import com.hotel.server.exception.IllegalException;
import com.hotel.server.exception.NoUserException;
import com.hotel.server.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * sa-token异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalException {

    // 拦截：用户不存在异常
    @ExceptionHandler(NoUserException.class)
    public R handlerException(NoUserException e) {
        log.error("用户不存在：", e);
        return R.error(HttpCode.NO_USER, e.getMessage());
    }

    // 拦截：指定参数不存在异常
    @ExceptionHandler(NotFoundException.class)
    public R handlerException(NotFoundException e) {
        log.error("指定参数不存在：", e);
        return R.error(HttpCode.NOT_FOUND, e.getMessage());
    }

    // 拦截：用户非法操作
    @ExceptionHandler(IllegalException.class)
    public R handlerException(IllegalException e) {
        log.error("用户非法操作：", e);
        return R.error(HttpCode.ILLEGAL_OPERATION, e.getMessage());
    }

    // 拦截：空指针异常
    @ExceptionHandler(NullPointerException.class)
    public R handlerException(NullPointerException e) {
        log.error("出现空指针异常: ", e);
        return R.error(e.getMessage());
    }

    // 拦截：其它所有异常
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R handlerException(MissingServletRequestParameterException e) {
        log.error("缺少参数异常: ", e);
        return new R<>(HttpCode.REQUEST_PARAM_BLANK, e.getMessage(), null);
    }

    // 拦截：其它所有异常
    @ExceptionHandler(Exception.class)
    public R handlerException(Exception e) {
        log.error("出现异常: ", e);
        return R.error(e.getMessage());
    }
}