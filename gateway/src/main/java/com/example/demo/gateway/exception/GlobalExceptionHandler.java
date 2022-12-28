package com.example.demo.gateway.exception;

import com.example.common.exception.BaseException;
import com.example.common.response.DefaultResponse;
import com.example.common.response.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 * 全局异常处理器
 */
@Slf4j
public class GlobalExceptionHandler extends DefaultErrorWebExceptionHandler {

    private final Tracer tracer;

    public GlobalExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                                  ErrorProperties errorProperties, ApplicationContext applicationContext, Tracer tracer) {
        super(errorAttributes, resources, errorProperties, applicationContext);
        this.tracer = tracer;
    }

    /**
     * 指定返回结构为 JSON 类型
     * @param errorAttributes 错误属性信息
     * @return 路由信息
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
       return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 组装返回结构
     * @param request 请求
     * @return 响应序列
     */
    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        /*
         * 默认的错误信息
         * e.g. :
         * {
         *      "timestamp": "2022-11-15T06:39:18.223+00:00",
         *      "path": "/test/2222main/d",
         *      "status": 404,
         *      "error": "Not Found",
         *      "message": null,
         *      "requestId": "a54901b"
         * }
         */
        Map<String, Object> errorMap = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        HttpStatus httpStatus;
        DefaultResponse defaultResponse;
        Throwable throwable = super.getError(request);
        log.error("#catch exception", throwable);
        if (throwable instanceof BaseException) {
            // 自定义异常（可预期，直接返回 200）
            httpStatus = HttpStatus.OK;
            defaultResponse = ((BaseException) throwable).getDefaultResponse();
        } else {
            // 其他异常
            ReturnCodeEnum returnCodeEnum;
            int statusCode = getHttpStatus(errorMap);
            httpStatus = HttpStatus.valueOf(statusCode);
            if (httpStatus.is4xxClientError()) {
                returnCodeEnum = ReturnCodeEnum.CLIENT_ERROR;
            } else {
                returnCodeEnum = ReturnCodeEnum.SERVER_ERROR;
            }
            defaultResponse = DefaultResponse.fail(returnCodeEnum);
        }

        // 设置 tranceId
        Span span = tracer.currentSpan();
        if (Objects.nonNull(span)) {
            String tranceId = span.context().traceId();
            defaultResponse.setTraceId(tranceId);
        }

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(defaultResponse));
    }
}
