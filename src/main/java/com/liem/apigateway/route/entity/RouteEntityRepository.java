package com.liem.apigateway.route.entity;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static com.liem.apigateway.route.GatewayConfiguration.ROUTE_MODE;
import static com.liem.apigateway.route.GatewayConfiguration.ROUTE_MODE_DYNAMIC;

@ConditionalOnProperty(
        value       = ROUTE_MODE,
        havingValue = ROUTE_MODE_DYNAMIC
)
@Repository
public interface RouteEntityRepository
        extends ReactiveCrudRepository<RouteEntity, Long> {
    Mono<RouteEntity> findByServiceNameAndActiveIsTrue(String serviceName);
}
