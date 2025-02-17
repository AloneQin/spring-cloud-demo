package com.example.demo.gateway.filter;

import com.example.common.utils.JacksonUtils;
import com.example.demo.gateway.common.constants.enums.FilterOrderEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

/**
 * 请求体过滤器
 */
@Slf4j
@Component
public class RequestBodyFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("#filter, uri:{}, source uri:{}", exchange.getRequest().getURI(), getURI(exchange));
        parseBody(exchange);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.REQUEST_BODY.value;
    }

    /**
     * 获取请求地址
     * <p> 当请求被缓存后，会自动调用 {@link RewritePathGatewayFilterFactory} 进行地址重写，导致最终路径会去除路由上下文前缀，此方法用来获取源地址
     * @param exchange 请求交换契约
     * @return 源地址
     */
    private URI getURI(ServerWebExchange exchange) {
        AtomicReference<URI> atmUri = new AtomicReference<>(exchange.getRequest().getURI());
        Set<URI> uriSet = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        Optional.ofNullable(uriSet).orElse(Collections.emptySet()).stream().findFirst().ifPresent(atmUri::set);
        return atmUri.get();
    }

    /**
     * 解析请求体
     * @param exchange 请求交换契约
     */
    private void parseBody(ServerWebExchange exchange) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String bodyStr = "";
        MediaType contentType = exchange.getRequest().getHeaders().getContentType();
        DataBuffer dataBuffer = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
        if (Objects.nonNull(dataBuffer)) {
            bodyStr = dataBuffer.toString(StandardCharsets.UTF_8);
            if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
                map = parseMultipartFormData(bodyStr, contentType);
            } else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
                map = parseApplicationFormUrlencoded(bodyStr);
            }
        }
        if (CollectionUtils.isEmpty(map)) {
            log.info("#parseBody, body str: \n{}", bodyStr);
        } else {
            log.info("#parseBody, body map: \n{}", JacksonUtils.toStringFormat(map));
        }
    }

    /**
     * 解析 multipart/form-data
     * @param bodyStr 请求体字符串
     * @param contentType 内容类型
     * @return 参数键值对
     */
    private LinkedHashMap<String, String> parseMultipartFormData(String bodyStr, MediaType contentType) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        bodyStr = bodyStr.replaceAll("\r\n", "");
        String boundary = contentType.getParameter("boundary");
        if (Objects.isNull(boundary)) {
            return map;
        }
        String[] parts = bodyStr.split(boundary);
        for (String str : parts) {
            // 一条正确数据固定格式：Content-Disposition: form-data; name=...--
            if (!(str.startsWith("Content-Disposition: form-data; name=") && str.endsWith("--"))) {
                continue;
            }
            // Content-Disposition: form-data; name="xxx"yyy-- -> "xxx"yyy
            str = str.replaceAll("Content-Disposition: form-data; name=", "")
                    .replaceAll("--", "");
            int keyStartIndex = str.indexOf("\"");
            int keyEndIndex = str.indexOf("\"", keyStartIndex + 1);
            // "xxx"yyy -> xxx
            String key = str.substring(keyStartIndex + 1, keyEndIndex);
            // 文件参数
            if (str.contains("filename=\"")) {
                int filenameStartIndex = str.indexOf("\"", keyEndIndex + 1);
                int filenameEndIndex = str.indexOf("\"", filenameStartIndex + 1);
                // "xxx"; filename="logo.png"... -> logo.png
                String filename = str.substring(filenameStartIndex + 1, filenameEndIndex);
                map.put(key, filename);
            }
            // 文本参数
            else {
                // "xxx"yyy -> yyy
                String value = str.substring(keyEndIndex + 1);
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 解析 x-www-form-urlencoded
     * @param bodyStr 请求体字符串
     * @return 参数键值对
     */
    private LinkedHashMap<String, String> parseApplicationFormUrlencoded(String bodyStr) {
        return Arrays.stream(bodyStr.split("&")).collect(Collectors.toMap(s -> s.split("=")[0], s -> s.split("=")[1], (v1, v2) -> v2, LinkedHashMap::new));
    }
}
