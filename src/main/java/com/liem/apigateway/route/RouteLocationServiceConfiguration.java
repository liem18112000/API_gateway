package com.liem.apigateway.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class RouteLocationServiceConfiguration {

    public static final String STATIC_SERVICE = "static";

    @Primary
    @Bean(STATIC_SERVICE)
    public RouteLocatorService getStaticService(
            @Autowired Environment environment) {
        log.info("StaticRouteLocatorService is initialized");
        return new StaticRouteLocatorService(environment);
    }
}
