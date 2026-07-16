package com.denizbyrk.sffecommerce.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;

import reactor.core.publisher.Mono;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

@Component
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("Gateway Request: {} {} from {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                exchange.getRequest().getRemoteAddress());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Gateway Response: {} - Status: {}",
                    exchange.getRequest().getURI(),
                    exchange.getResponse().getStatusCode());
        }));
    }

    @Override
    public int getOrder() {

        return -1;
    }
}