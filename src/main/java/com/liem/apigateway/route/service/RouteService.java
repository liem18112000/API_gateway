package com.liem.apigateway.route.service;

import com.liem.apigateway.route.entity.RouteDTO;
import reactor.core.publisher.Flux;

public interface RouteService {

    Flux<RouteDTO> getAllRoutes();
}
