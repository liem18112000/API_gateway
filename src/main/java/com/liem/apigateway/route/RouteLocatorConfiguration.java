package com.liem.apigateway.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteLocatorConfiguration {

    @Autowired
    private RouteLocatorService routeLocatorService;

    @Bean
    public RouteLocator configRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> this.routeLocatorService.buildRoute(p, "ingredient"))
                .route(p -> this.routeLocatorService.buildRoute(p, "recipe"))
                .route(p -> this.routeLocatorService.buildRoute(p, "supplier"))
                .route(p -> this.routeLocatorService.buildRoute(p, "notification"))
                .route(p -> this.routeLocatorService.buildRoute(p, "security"))
                .build();
    }
}
