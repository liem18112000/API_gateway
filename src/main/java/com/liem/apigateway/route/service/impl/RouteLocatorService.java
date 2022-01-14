package com.liem.apigateway.route.service.impl;

import com.liem.apigateway.route.service.RouteFilter;
import com.liem.apigateway.route.service.RouteService;
import com.liem.apigateway.route.entity.RouteDTO;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class RouteLocatorService implements RouteLocator {
    
    private final RouteService routeService;
    
    private final RouteLocatorBuilder.Builder routeBuilder;

    private final RouteFilter routeFilter;

    public RouteLocatorService(
            RouteService routeService,
            RouteLocatorBuilder routeBuilder,
            RouteFilter routeFilter) {
        this.routeService = routeService;
        this.routeBuilder = routeBuilder.routes();
        this.routeFilter = routeFilter;
    }
    
    @Override
    public Flux<Route> getRoutes() {
        return routeService.getAllRoutes()
                .map(route -> routeBuilder.route(predicate -> setPredicate(route, predicate)))
                .collectList().flatMapMany(builders -> routeBuilder.build().getRoutes());
    }

    private Buildable<Route> setPredicate(RouteDTO route, PredicateSpec predicate) {
        return predicate.path(route.getPath())
                .filters(this.routeFilter::filter)
                .uri(route.getUri());
    }
}
