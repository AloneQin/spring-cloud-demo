package com.example.demo.gateway.common;

import lombok.AllArgsConstructor;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators;
import org.springframework.web.server.ServerWebExchange;

/**
 * WebFluxSleuthLog 日志器
 * 为提高性能，当 ${spring.sleuth.reactor.instrumentation-type} 取值设置为 Manual 模式时，需要通过如下方法打印带有 traceId 的日志
 * webFluxSleuthLog.log(exchange, () -> log.info("do something"));
 */
@AllArgsConstructor
public class WebFluxSleuthLog {

    private final Tracer tracer;

    private final CurrentTraceContext currentTraceContext;

    public void log(ServerWebExchange exchange, Runnable runnable) {
        WebFluxSleuthOperators.withSpanInScope(tracer, currentTraceContext, exchange, runnable);
    }
}
