package com.example.common.exception;

import com.example.common.response.DefaultResponse;
import com.example.common.response.ReturnCodeEnum;
import lombok.Getter;

/**
 * 自定义全局异常基类
 */
@Getter
public class BaseException extends RuntimeException {

    private DefaultResponse defaultResponse;

    private BaseException(String message) {
        super(message);
    }

    public BaseException(DefaultResponse defaultResponse) {
        this(defaultResponse.detailMessage());
        this.defaultResponse = defaultResponse;
    }

    public BaseException(ReturnCodeEnum returnCodeEnum) {
        this(DefaultResponse.fail(returnCodeEnum));
    }

    public BaseException(String code, String message, Object content) {
        this(DefaultResponse.fail(code, message, content));
    }
}
