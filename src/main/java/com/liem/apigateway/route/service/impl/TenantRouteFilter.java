package com.liem.apigateway.route.service.impl;

import com.liem.apigateway.route.service.RouteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TenantRouteFilter implements RouteFilter {

    @Autowired
    private Environment environment;

    @Override
    public GatewayFilterSpec filter(GatewayFilterSpec filterSpec) {
        return filterSpec.addRequestHeader("tenantId",
                environment.getProperty("domain.tenant.id", String.class, "1"));
    }
}
