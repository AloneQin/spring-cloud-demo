package com.example.common.filter;

import com.example.common.config.CustomFilterProperties;
import com.example.common.response.DefaultResponse;
import com.example.common.response.ReturnCodeEnum;
import com.example.common.utils.ResponseUtils;
import com.example.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认过滤器
 * 1.拦截请求来源
 * 2.记录请求 URL
 */
@Slf4j
public class DefaultFilter implements Filter {

    private static final String TRACE_ID = "x-b3-traceid";

    private final CustomFilterProperties customFilterProperties;

    public DefaultFilter(CustomFilterProperties customFilterProperties) {
        this.customFilterProperties = customFilterProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String contentType = request.getContentType();
        String traceId = request.getHeader(TRACE_ID);
        if (StringUtils.isBlank(traceId) && customFilterProperties.getTraceIdEnable()) {
            // 当请求来源不为网关或其他同源微服务，禁止通行
            log.warn("#doFilter, source error, method: {}, uri: {}, contentType: {}", method, uri, contentType);
            ResponseUtils.outputJson(response, HttpStatus.FORBIDDEN.value(), DefaultResponse.fail(ReturnCodeEnum.CLIENT_ERROR));
            return;
        }

        log.info("#doFilter, method: {}, uri: {}, contentType: {}", method, uri, contentType);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
