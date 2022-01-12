package com.liem.apigateway.route;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;

public interface RouteLocatorService {

    Buildable<Route> buildRoute(PredicateSpec p, String domain);
}
