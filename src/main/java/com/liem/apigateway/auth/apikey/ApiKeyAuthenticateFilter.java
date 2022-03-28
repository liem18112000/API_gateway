package com.liem.apigateway.auth.apikey;

import com.liem.apigateway.client.AuthClient;
import com.liem.apigateway.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.liem.apigateway.config.auth.AuthConfig.EXCLUDED_AUTHORIZE_PATH;
import static com.liem.apigateway.config.auth.AuthConfig.HEADER_API_KEY;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ApiKeyAuthenticateFilter implements WebFilter {

    private final AuthClient authClient;

    private final RouteService routeService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        final var request = exchange.getRequest();
        final var path = request.getPath().toString();
        final var response = exchange.getResponse();
        final var headers = request.getHeaders();
        final var apiKey = headers.get(HEADER_API_KEY);
//        final var apiPrincipal = headers.get(HEADER_API_PRINCIPAL);

        if (EXCLUDED_AUTHORIZE_PATH.contains(path)) {
            return chain.filter(exchange);
        }

        return routeService.getAllRoutes().collectList().flatMap(allRoute -> {

            AtomicBoolean isMatch = new AtomicBoolean(false);
            AntPathMatcher matcher = new AntPathMatcher("/");
            allRoute.forEach(route -> {
                if (matcher.match(route.getPath(), path) && route.isAuth() && route.isActive()) {
                    isMatch.set(true);
                }
            });

            if (isMatch.get()) {
                if (Objects.isNull(apiKey) || apiKey.isEmpty()) {
                    log.warn("Api key is not found");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return Mono.empty();
                }
                return this.authClient.authorize(apiKey.get(0)).flatMap(r -> {
                    if(r.isSuccess()) {
                        return chain.filter(exchange);
                    }
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return Mono.empty();
                }).onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return Mono.empty();
                });
            }

            return chain.filter(exchange);
        });
    }
}
