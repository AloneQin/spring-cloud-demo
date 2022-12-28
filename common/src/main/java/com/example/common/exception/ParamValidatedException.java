package com.example.common.exception;

import com.example.common.response.DefaultResponse;
import com.example.common.response.ReturnCodeEnum;

import java.util.Collections;
import java.util.List;

/**
 * 参数校验异常
 */
public class ParamValidatedException extends BaseException {

    public ParamValidatedException(List<ParamError> paramErrorList) {
        super(DefaultResponse.fail(ReturnCodeEnum.PARAM_ERROR, paramErrorList));
    }

    public ParamValidatedException(ParamError paramError) {
        this(Collections.singletonList(paramError));
    }

}
