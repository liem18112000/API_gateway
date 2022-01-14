package com.liem.apigateway.gateway;

import com.liem.apigateway.config.ApiV1;
import com.liem.apigateway.gateway.service.GatewayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API, produces = ApiV1.MIME_API)
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping("route/refresh")
    public Mono<?> refreshRoute() {
        try {
            this.gatewayService.refreshRoutes();
            return ServerResponse.ok().build();
        } catch (Exception exception) {
            return ServerResponse.status(INTERNAL_SERVER_ERROR)
                    .bodyValue(exception.getStackTrace());
        }
    }
}
