package com.hotel.common.constants;

/**
 * 自定义http状态码
 *
 * @author: 郭晨旭
 * @create: 2023-10-30 09:41
 * @version: 1.0
 */
public interface HttpCode {
    Integer SUCCESS = 200; //成功
    Integer PARAM_WRONG = 400; // 参数错误
    Integer REQUEST_PARAM_BLANK = 401; // 传入参数为空
    Integer NOT_REGISTER = 402; // 未注册
    Integer FORBIDDEN = 403; // 禁止访问
    Integer NOT_FOUND = 403; // 指定参数不存在
    Integer SERVER_WRONG = 500; // 服务器错误
    Integer NETWORK_WRONG = 1000; // 网络异常
    Integer NO_USER = 1001; // 用户不存在
    Integer VISIT_LIMIT = 1002; // 访问频繁
    Integer ILLEGAL_OPERATION = 1003; // 用户非法操作
    Integer WS_SUCCESS = 2000; // ws正常关闭
}
