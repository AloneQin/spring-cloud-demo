package com.example.demo.gateway.common.constants.enums;

import com.example.demo.gateway.filter.CacheRequestBodyFilter;
import com.example.demo.gateway.filter.GatewayLogFilter;
import com.example.demo.gateway.filter.RequestBodyFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;

/**
 * 过滤器顺序 枚举
 */
@AllArgsConstructor
public enum FilterOrderEnum {

    /**
     * 缓存请求体
     */
    CACHE_REQUEST_BODY  (0, CacheRequestBodyFilter.class),

    /**
     * 请求体
     */
    REQUEST_BODY        (1, RequestBodyFilter.class),

    /**
     * 网关日志
     */
    GATEWAY_LOG         (Ordered.LOWEST_PRECEDENCE, GatewayLogFilter.class)
    ;

    /**
     * 顺序值
     */
    public final int value;

    /**
     * 过滤器类
     */
    public final Class<? extends GlobalFilter> clazz;

}
