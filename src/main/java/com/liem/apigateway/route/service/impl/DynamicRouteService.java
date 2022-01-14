package com.liem.apigateway.route.service.impl;

import com.liem.apigateway.route.service.RouteService;
import com.liem.apigateway.route.entity.RouteDTO;
import com.liem.apigateway.route.entity.RouteEntity;
import com.liem.apigateway.route.entity.RouteEntityRepository;
import com.liem.apigateway.route.entity.RouteMapper;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class DynamicRouteService implements RouteService {

    private final RouteEntityRepository routeRepository;

    private final RouteMapper routeMapper;

    public DynamicRouteService(
            RouteEntityRepository routeRepository, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    @Override
    public Flux<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().filter(RouteEntity::isActive)
                .map(entity -> {
                    final var dto = this.routeMapper.toDto(entity);
                    log.info("Mapping {} - {}", dto.getUri(), dto.getPath());
                    return dto;
                });
    }
}
