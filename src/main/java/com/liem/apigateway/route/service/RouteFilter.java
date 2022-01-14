package com.liem.apigateway.route.service;

import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;

public interface RouteFilter {

    GatewayFilterSpec filter(GatewayFilterSpec filterSpec);
}
