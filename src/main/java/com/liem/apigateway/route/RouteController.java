package com.liem.apigateway.route;

import com.liem.apigateway.config.ApiV1;
import com.liem.apigateway.gateway.service.GatewayService;
import com.liem.apigateway.route.entity.RouteDTO;
import com.liem.apigateway.route.service.RouteService;
import io.sentry.spring.tracing.SentryTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SentryTransaction(operation = "route-query")
@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API + "/route", produces = ApiV1.MIME_API)
public class RouteController {

    private final RouteService routeService;
    private final GatewayService gatewayService;

    @GetMapping
    public Flux<?> getAllRoutes() {
        return this.routeService.getAllRoutes();
    }

    @PostMapping
    public Mono<?> createRoute(@RequestBody RouteDTO request) {
        this.gatewayService.refreshRoutes();
        return Mono.just(request);
    }

    @GetMapping("{name}")
    public Mono<?> getRoute(@PathVariable String name) {
        return Mono.just(this.routeService.getByServiceName(name));
    }

    @PutMapping("{name}")
    public Mono<?> updateRoute(@PathVariable String name) {
        this.gatewayService.refreshRoutes();
        // TODO: Add logic
        return Mono.just(this.routeService.getByServiceName(name));
    }
}
