package com.example.common.config;

import com.example.common.filter.DefaultFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 默认过滤器配置类
 */
@Configuration
@AllArgsConstructor
public class DefaultFilterConfig {

    private final CustomFilterProperties customFilterProperties;

    /**
     * URL 过滤器注册
     * @return 过滤器注册实例
     */
    @Bean
    public FilterRegistrationBean<DefaultFilter> urlFilterRegistrationBean() {
        FilterRegistrationBean<DefaultFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DefaultFilter(customFilterProperties));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
