package com.example.common.response;

import brave.Span;
import brave.Tracer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 返回结构 TraceId 通知处理
 */
@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class TraceIdResponseBodyAdvice implements ResponseBodyAdvice {

    private final Tracer tracer;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (!(body instanceof DefaultResponse)) {
            return body;
        }

        DefaultResponse defaultResponse = (DefaultResponse) body;
        if (Objects.nonNull(defaultResponse.getTraceId())) {
            return body;
        }

        Span span = tracer.currentSpan();
        if (Objects.nonNull(span)) {
            String tranceId = span.context().traceIdString();
            defaultResponse.setTraceId(tranceId);
        }

        return defaultResponse;
    }
}
