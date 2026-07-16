package com.denizbyrk.sffecommerce.api_gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
@Slf4j
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        log.info("Configuring Gateway routes");

        return builder.routes()

                ///
                ///user service routing
                ///
                .route("user-service", r -> r
                        .path("/api/user/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway-Request", "API Gateway")
                                .addResponseHeader("X-Gateway-Request", "API Gateway"))
                                .uri("lb://user-service"))

                .route("user-service-admin", r -> r
                        .path("/api/admin/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway-Request", "API Gateway")
                                .addResponseHeader("X-Gateway-Request", "API Gateway"))
                        .uri("lb://user-service"))

                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway-Request", "API Gateway")
                                .addResponseHeader("X-Gateway-Request", "API Gateway"))
                        .uri("lb://user-service"))

                ///
                /// product service routing
                ///
                .route("product-service", r -> r
                        .path("/api/product/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway-Request", "API Gateway")
                                .addResponseHeader("X-Gateway-Request", "API Gateway"))
                        .uri("lb://product-service"))

                ///
                /// order service routing
                ///
                .route("order-service", r -> r
                        .path("/api/order/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway-Request", "API Gateway")
                                .addResponseHeader("X-Gateway-Request", "API Gateway"))
                        .uri("lb://order-service"))

                ///
                /// payment service routing
                ///
                .route("payment-service", r -> r
                        .path("/api/payment/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway-Request", "API Gateway")
                                .addResponseHeader("X-Gateway-Request", "API Gateway"))
                        .uri("lb://payment-service"))

                ///
                /// inventory service routing
                ///
                .route("inventory-service", r -> r
                        .path("/api/inventory/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway-Request", "API Gateway")
                                .addResponseHeader("X-Gateway-Request", "API Gateway"))
                        .uri("lb://inventory-service"))

                ///
                /// swagger routing
                ///
                .route("user-service-docs", r -> r
                        .path("/user-service/v3/api-docs")
                        .filters(f -> f.rewritePath(
                                "/user-service/v3/api-docs",
                                "/v3/api-docs"
                        ))
                        .uri("lb://user-service"))

                .route("product-service-docs", r -> r
                        .path("/product-service/v3/api-docs")
                        .filters(f -> f.rewritePath(
                                "/product-service/v3/api-docs",
                                "/v3/api-docs"
                        ))
                        .uri("lb://product-service"))

                .route("order-service-docs", r -> r
                        .path("/order-service/v3/api-docs")
                        .filters(f -> f.rewritePath(
                                "/order-service/v3/api-docs",
                                "/v3/api-docs"
                        ))
                        .uri("lb://order-service"))
                .route("payment-service-docs", r -> r
                        .path("/payment-service/v3/api-docs")
                        .filters(f -> f.rewritePath(
                                "/payment-service/v3/api-docs",
                                "/v3/api-docs"
                        ))
                        .uri("lb://payment-service"))
                .route("inventory-service-docs", r -> r
                        .path("/inventory-service/v3/api-docs")
                        .filters(f -> f.rewritePath(
                                "/inventory-service/v3/api-docs",
                                "/v3/api-docs"
                        ))
                        .uri("lb://inventory-service"))
                .build();
    }
}