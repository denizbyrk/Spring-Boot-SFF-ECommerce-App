package com.denizbyrk.sffecommerce.api_gateway.filter;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

@Component
public class JwtForwardFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String token = exchange.getRequest()
                        .getHeaders()
                        .getFirst("Authorization");

        if(token != null) {

            exchange = exchange.mutate()
                    .request(request -> request
                            .header("Authorization", token))
                    .build();
        }

        return chain.filter(exchange);
    }
}