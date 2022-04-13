package com.liem.apigateway.route.service;

import com.liem.apigateway.route.entity.RouteDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteService {
    Flux<RouteDTO> getAllRoutes();
    Mono<RouteDTO> getByServiceName(String name);
}
