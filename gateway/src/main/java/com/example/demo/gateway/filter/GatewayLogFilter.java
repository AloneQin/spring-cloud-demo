package com.example.demo.gateway.filter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 网关日志过滤器
 * 打印网关请求、路由、响应相关日志
 */
@Slf4j
@Component
@RefreshScope
public class GatewayLogFilter implements GlobalFilter, Ordered {

    @Value("${custom.gateway.log-type}")
    private Integer gatewayLogType;

    private final Tracer tracer;

    public GatewayLogFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();
        URI originalUri = request.getURI();
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        URI forwardUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String traceId = getTraceId();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n----------------------------------- Gateway Logs -----------------------------------");
        stringBuilder.append("\n[Request]");
        stringBuilder.append("\n--Trace Id          : {}");
        stringBuilder.append("\n--Request Id        : {}");
        stringBuilder.append("\n--Method            : {}");
        stringBuilder.append("\n--URL               : {}");
        stringBuilder.append("\n--Forward URL       : {}");
        stringBuilder.append("\n--Status Code       : {}");
        stringBuilder.append("\n[Route]");
        stringBuilder.append("\n--Route Id          : {}");
        stringBuilder.append("\n--Route URI         : {}");
        stringBuilder.append("\n--Route Predicate   : {}");
        stringBuilder.append("\n--Route Filter      : {}");

        ArrayList<Object> argList = new ArrayList<>();
        argList.add(traceId);
        argList.add(request.getId());
        argList.add(request.getMethodValue());
        argList.add(originalUri.toString());
        argList.add(forwardUri.toString());
        argList.add(exchange.getResponse().getStatusCode());
        argList.add(route.getId());
        argList.add(route.getUri().toString());
        argList.add(route.getPredicate().toString());
        argList.add(route.getFilters().toString());

        Mono<Void> mono = chain.filter(exchange);

        if (GatewayLogTypeEnum.IGNORE_RESPONSE.value.equals(gatewayLogType)
                || GatewayLogTypeEnum.SPLIT_RESPONSE.value.equals(gatewayLogType)) {
            stringBuilder.append("\n------------------------------------------------------------------------------------");
            log.info(stringBuilder.toString(), argList.toArray());
        }

        if (GatewayLogTypeEnum.MERGE_RESPONSE.value.equals(gatewayLogType)
                || GatewayLogTypeEnum.SPLIT_RESPONSE.value.equals(gatewayLogType)) {
            mono = mono.then(
                    Mono.fromRunnable(() -> {
                        if (GatewayLogTypeEnum.SPLIT_RESPONSE.value.equals(gatewayLogType)) {
                            stringBuilder.delete(0, stringBuilder.length());
                            argList.removeAll(argList);
                            stringBuilder.append("\n----------------------------------- Gateway Logs -----------------------------------");
                        }
                        stringBuilder.append("\n[Response]");
                        stringBuilder.append("\n--Trace Id          : {}");
                        stringBuilder.append("\n--Request Id        : {}");
                        stringBuilder.append("\n--Status Code       : {}");
                        stringBuilder.append("\n--Total Time        : {} s");
                        stringBuilder.append("\n------------------------------------------------------------------------------------");

                        ServerHttpResponse response = exchange.getResponse();
                        float totalTime = (System.currentTimeMillis() - startTime)/1000.00f;
                        argList.add(traceId);
                        argList.add(exchange.getRequest().getId());
                        argList.add(response.getStatusCode().toString());
                        argList.add(totalTime);
                        log.info(stringBuilder.toString(), argList.toArray());
                    })
            );
        }
        return mono;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private String getTraceId() {
        Span span = tracer.currentSpan();
        if (Objects.nonNull(span)) {
            return span.context().traceId();
        }
        return "";
    }

    /**
     * 网关日志类型枚举
     */
    @AllArgsConstructor
    public enum GatewayLogTypeEnum {

        /**
         * 忽略响应信息
         * 打印请求、路由信息
         */
        IGNORE_RESPONSE(1),
        /**
         * 合并响应信息
         * 打印请求、路由、响应信息，在响应返回时统一打印，存在延时
         */
        MERGE_RESPONSE(2),
        /**
         * 分割响应信息
         * 打印请求、路由、响应信息，独立打印响应信息，无延时
         */
        SPLIT_RESPONSE(3),
        ;

        public Integer value;
    }
}
