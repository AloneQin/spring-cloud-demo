package com.example.demo.gateway.filter;

import com.example.demo.gateway.common.constants.enums.FilterOrderEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * RequestBody 只能读取一次，要保证后面使用，需要先进行缓存
 */
@Slf4j
@Component
public class CacheRequestBodyFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ServerWebExchangeUtils.cacheRequestBody(exchange, (serverHttpRequest) -> {
            if (Objects.equals(serverHttpRequest, exchange.getRequest())) {
                return chain.filter(exchange);
            }
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        });
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.CACHE_REQUEST_BODY.value;
    }
}
