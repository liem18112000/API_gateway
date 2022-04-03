package com.liem.apigateway.auth.apikey;

import com.liem.apigateway.client.AuthClient;
import com.liem.apigateway.route.entity.RouteDTO;
import com.liem.apigateway.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.liem.apigateway.config.AppConfig.X_API_KEY_HEADER;
import static com.liem.apigateway.config.AppConfig.X_PRINCIPAL_HEADER;
import static com.liem.apigateway.config.auth.AuthConfig.AUTHORIZATION_HTTP_METHODS;
import static com.liem.apigateway.config.auth.AuthConfig.EXCLUDED_AUTHORIZE_PATH;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ApiKeyAuthenticateFilter implements WebFilter {

    private final AuthClient authClient;

    private final RouteService routeService;

    private final Set<String> cachedKeys = ConcurrentHashMap.newKeySet();

    private final Map<String, String> cachedKeysWithPrincipals = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        final var request = exchange.getRequest();
        final var response = exchange.getResponse();
        final var path = request.getPath().toString();

        if (EXCLUDED_AUTHORIZE_PATH.contains(path)) {
            return chain.filter(exchange);
        }

        return processFilter(exchange, chain, path, request, response);
    }

    private Mono<Void> processFilter(
            ServerWebExchange exchange,
            WebFilterChain chain,
            String path,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        final var httpMethod = request.getMethod();
        final var headers = request.getHeaders();
        final var apiKey = headers.get(X_API_KEY_HEADER);

        if (HttpMethod.OPTIONS.equals(httpMethod)) {
            return chain.filter(exchange);
        }

        return routeService.getAllRoutes().collectList().flatMap(allRoute -> {
            boolean isMatch = checkRouteMatch(path, allRoute).get();
            if (isMatch) {
                if (Objects.isNull(apiKey) || apiKey.isEmpty()) {
                    log.warn("Api key is not found");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return Mono.empty();
                }

                // Check for applied authorization on methods
                if (AUTHORIZATION_HTTP_METHODS.contains(httpMethod)) {

                    // Run authorization with api key and principal
                    final var apiPrincipal = headers.get(X_PRINCIPAL_HEADER);

                    if (Objects.isNull(apiPrincipal) || apiPrincipal.isEmpty()) {
                        log.warn("Api principal is not found");
                        response.setStatusCode(HttpStatus.FORBIDDEN);
                        return Mono.empty();
                    }

                    return this.authenticateWithPrincipal(
                            exchange, chain, response, apiKey.get(0), apiPrincipal.get(0));

                }

                // Run authenticate with api key
                return this.authenticate(exchange, chain, response, apiKey.get(0));
            }

            return chain.filter(exchange);
        });
    }

    private Mono<Void> authenticateWithPrincipal(
            ServerWebExchange exchange,
            WebFilterChain chain,
            ServerHttpResponse response,
            String apiKey,
            String apiPrincipal
    ) {

        if (cachedKeysWithPrincipals.containsKey(apiKey)) {
            final var cachedPrincipal = cachedKeysWithPrincipals.get(apiKey);
            if (cachedPrincipal.equals(apiPrincipal)) {
                log.info("API key with principal is found in cache");
                return chain.filter(exchange);
            }
        }

        return this.authClient.authenticate(apiKey, apiPrincipal).flatMap(r -> {
            if (r.isSuccess()) {
                log.info("API key with principal is valid and will be cached");
                cachedKeysWithPrincipals.put(apiKey, apiPrincipal);
                return chain.filter(exchange);
            }
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return Mono.empty();
        }).onErrorResume(throwable -> {
            throwable.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        });
    }

    private Mono<Void> authenticate(
            ServerWebExchange exchange,
            WebFilterChain chain,
            ServerHttpResponse response,
            String apiKey
    ) {
        if (this.cachedKeys.contains(apiKey)) {
            log.info("Api key is found in cache");
            return chain.filter(exchange);
        }

        return this.authClient.authorize(apiKey).flatMap(r -> {
            if (r.isSuccess()) {
                log.info("Api key is valid and will be cached");
                this.cachedKeys.add(apiKey);
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

    private AtomicBoolean checkRouteMatch(String path, List<RouteDTO> allRoute) {
        AtomicBoolean isMatch = new AtomicBoolean(false);
        AntPathMatcher matcher = new AntPathMatcher("/");
        allRoute.forEach(route -> {
            if (matcher.match(route.getPath(), path) && route.isAuth() && route.isActive()) {
                isMatch.set(true);
            }
        });
        return isMatch;
    }
}
