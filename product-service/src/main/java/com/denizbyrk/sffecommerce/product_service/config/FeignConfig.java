package com.denizbyrk.sffecommerce.product_service.config;

import feign.RequestInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${internal.service-key}")
    private String serviceKey;

    @Bean
    public RequestInterceptor requestInterceptor(){

        return requestTemplate -> {

            System.out.println("Adding service key");

            requestTemplate.header("X-Service-Key", this.serviceKey);
        };
    }
}