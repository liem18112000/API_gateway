package com.liem.apigateway.route;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;

@Slf4j
@Configuration
public class RouteLocatorConfiguration {

    public static final String ROUTE_CONFIG_BASE_URL = "domain.base-url";

    public static final String TENANT_HEADER_NAME = "tenantId";

    public static final String ROUTE_CONFIG_TENANT_ID = "domain.tenant.id";

    @SneakyThrows
    private String getUri(@NotBlank final String domain) {
        final var configKey =  ROUTE_CONFIG_BASE_URL.concat(".").concat(domain);
        final var domainUri = this.env.getProperty(configKey, String.class);
        if(StringUtils.hasText(domainUri)) {
            final var finalDomainUri = domainUri.concat("/**");
            log.info("Mapping {} - {}", finalDomainUri, getPath(domain));
            return finalDomainUri;
        } else {
            throw new Exception("Properties not found : ".concat(configKey));
        }
    }

    private String getPath(@NotBlank final String domain) {
        return "/".concat(domain).concat("/**");
    }

    private String getTenantId() {
        return env.getProperty(ROUTE_CONFIG_TENANT_ID, String.class);
    }

    @Autowired
    private Environment env;

    @Bean
    public RouteLocator configRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> buildRoute(p, "ingredient"))
                .route(p -> buildRoute(p, "recipe"))
                .route(p -> buildRoute(p, "supplier"))
                .route(p -> buildRoute(p, "notification"))
                .route(p -> buildRoute(p, "security"))
                .build();
    }

    private Buildable<Route> buildRoute(PredicateSpec p, String ingredient) {
        return p.path(getPath(ingredient))
                .filters(this::addTenantIdToHeader)
                .uri(getUri(ingredient));
    }

    private GatewayFilterSpec addTenantIdToHeader(GatewayFilterSpec f) {
        return f.addRequestHeader(TENANT_HEADER_NAME, getTenantId());
    }
}
