package com.congge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CrosConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        //是否允许请求带有验证信息,比如token之类的
        config.setAllowCredentials(true);
        //允许访问的客户端域名
        config.addAllowedOrigin("*");
        //允许客户端请求时携带header信息
        config.addAllowedHeader("*");
        //允许请求时的方法类型
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource urlConfig = new UrlBasedCorsConfigurationSource();
        urlConfig.registerCorsConfiguration("/**", config);
        return new CorsFilter(urlConfig);
    }

}
