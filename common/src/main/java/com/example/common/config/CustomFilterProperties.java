package com.example.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义过滤器属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "custom.filter")
public class CustomFilterProperties {

    private Boolean traceIdEnable = true;

}
