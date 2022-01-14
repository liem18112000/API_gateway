package com.liem.apigateway.gateway.service.impl;

import com.liem.apigateway.gateway.service.GatewayService;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ApiGatewayService implements GatewayService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ApiGatewayService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void refreshRoutes() {
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
