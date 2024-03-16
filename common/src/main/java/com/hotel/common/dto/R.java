package com.hotel.common.dto;

import com.hotel.common.constants.HttpCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 统一返回类
 *
 * @author: guochenxu
 * @create: 2023-10-25 11:23
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("统一返回类")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("错误码")
    private Integer code;

    @ApiModelProperty("错误信息")
    private String message;

    @ApiModelProperty("数据")
    private T data;

    public static <T> R<T> success() {
        return new R<>(HttpCode.SUCCESS, "执行成功", null);
    }

    public static <T> R<T> success(String _msg) {
        return new R<>(HttpCode.SUCCESS, _msg, null);
    }

    public static <T> R<T> success(T _data) {
        return new R<>(HttpCode.SUCCESS, "执行成功", _data);
    }

    public static <T> R<T> success(String _msg, T _data) {
        return new R<>(HttpCode.SUCCESS, _msg, _data);
    }

    public static <T> R<T> error() {
        return new R<>(HttpCode.NETWORK_WRONG, "网络异常, 执行失败", null);
    }

    public static <T> R<T> error(String _msg) {
        return new R<>(HttpCode.NETWORK_WRONG, _msg, null);
    }


    public static <T> R<T> error(T _data) {
        return new R<>(HttpCode.NETWORK_WRONG, "网络异常, 执行失败", _data);
    }

    public static <T> R<T> error(String _msg, T _data) {
        return new R<>(HttpCode.NETWORK_WRONG, _msg, _data);
    }

    public static <T> R<T> error(Integer _code, String _msg) {
        return new R<>(_code, _msg, null);
    }
}
