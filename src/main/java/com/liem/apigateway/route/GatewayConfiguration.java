package com.liem.apigateway.route;

import com.liem.apigateway.route.service.RouteService;
import com.liem.apigateway.route.entity.RouteEntityRepository;
import com.liem.apigateway.route.entity.RouteMapper;
import com.liem.apigateway.route.service.impl.DynamicRouteService;
import com.liem.apigateway.route.service.impl.StaticRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GatewayConfiguration {

    public static final String ROUTE_MODE = "application.route-mode";
    public static final String ROUTE_MODE_STATIC = "static";
    public static final String ROUTE_MODE_DYNAMIC = "dynamic";

    @ConditionalOnProperty(
            value       = ROUTE_MODE,
            havingValue = ROUTE_MODE_STATIC
    )
    @Bean
    public RouteService getStaticService() {
        log.info("StaticRouteService is initialized");
        return new StaticRouteService();
    }

    @ConditionalOnProperty(
            value       = ROUTE_MODE,
            havingValue = ROUTE_MODE_DYNAMIC
    )
    @Bean
    public RouteService getDynamicService(
            @Autowired RouteEntityRepository repository,
            @Autowired RouteMapper mapper) {
        log.info("DynamicRouteService is initialized");
        return new DynamicRouteService(repository, mapper);
    }
}
