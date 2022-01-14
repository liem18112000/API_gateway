package com.liem.apigateway.route.entity;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Component
public class RouteMapper {

    public RouteDTO toDto(final RouteEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }

        return getRouteDTO(entity);
    }

    public RouteDTO toDtoWithException(final RouteEntity entity)
            throws IllegalArgumentException  {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity is null");
        }

        return getRouteDTO(entity);
    }

    private RouteDTO getRouteDTO(@NotNull final RouteEntity entity) {
        return RouteDTO.builder()
                .id(entity.getId())
                .serviceName(entity.getServiceName())
                .uri(entity.getUri())
                .path(entity.getPath())
                .active(entity.isActive())
                .auth(entity.isAuth())
                .build();
    }

    public RouteEntity toEntity(final RouteDTO routeDto) {

        if (Objects.isNull(routeDto)) {
            return null;
        }

        return getRouteEntity(routeDto);

    }

    public RouteEntity toEntityWithException(final RouteDTO routeDto)
            throws IllegalArgumentException {
        if (Objects.isNull(routeDto)) {
            throw new IllegalArgumentException("DTO is null");
        }

        return getRouteEntity(routeDto);
    }

    private RouteEntity getRouteEntity(@NotNull final RouteDTO routeDto) {
        return RouteEntity.builder()
                .id(routeDto.getId())
                .serviceName(routeDto.getServiceName())
                .uri(routeDto.getUri())
                .path(routeDto.getPath())
                .auth(routeDto.isAuth())
                .active(routeDto.isActive())
                .build();
    }
}
