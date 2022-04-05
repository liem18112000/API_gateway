package com.liem.apigateway.route.service.impl;

import com.liem.apigateway.route.service.RouteService;
import com.liem.apigateway.route.entity.RouteDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotBlank;

@Slf4j
public class StaticRouteService implements RouteService {

    @SneakyThrows
    private String getUri(@NotBlank final String domain) {
        return "lb://"
                .concat(domain.trim().replaceAll("endpoint/", ""))
                .concat("-service/**");
    }

    private String getPath(@NotBlank final String domain) {
        return "/".concat(domain.trim()).concat("/**");
    }

    /**
     * {@inheritDoc}
     */
    public Flux<RouteDTO> getAllRoutes() {
        return Flux.fromArray(new String[]{
                "ingredient", "recipe", "supplier", "notification", "security",
                "endpoint/ingredient", "endpoint/recipe", "endpoint/supplier", "endpoint/notification"
        }).map(this::getRouteDTO);
    }

    private RouteDTO getRouteDTO(String domain) {
        final var path = getPath(domain);
        final var uri = getUri(domain);
        final var isAuth = getAuth(domain);
        log.info("Mapping {} - {} - {} - {}", domain, uri, path, isAuth);
        return RouteDTO.builder()
                .serviceName(domain)
                .path(path)
                .uri(uri)
                .active(true)
                .auth(isAuth)
                .build();
    }

    private boolean getAuth(String domain) {
        return !domain.contains("endpoint") && !domain.contains("security");
    }


}
