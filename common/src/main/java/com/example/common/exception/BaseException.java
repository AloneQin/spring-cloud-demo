package com.example.common.exception;

import com.example.common.response.DefaultResponse;
import com.example.common.response.ReturnCode;
import com.example.common.response.ReturnCodeEnum;
import lombok.Getter;

/**
 * 自定义全局异常基类
 */
@Getter
public class BaseException extends RuntimeException {

    private DefaultResponse<?> defaultResponse;

    private BaseException(String message) {
        super(message);
    }

    public <T> BaseException(DefaultResponse<T> defaultResponse) {
        this(defaultResponse.detailMessage());
        this.defaultResponse = defaultResponse;
    }

    public BaseException(ReturnCode returnCode) {
        this(DefaultResponse.fail(returnCode));
    }

    public BaseException(ReturnCodeEnum returnCodeEnum, String message) {
        this(returnCodeEnum.getCode(), returnCodeEnum.getMessage() + ": " + message, null);
    }

    public <T> BaseException(String code, String message, T content) {
        this(DefaultResponse.fail(code, message, content));
    }
}
