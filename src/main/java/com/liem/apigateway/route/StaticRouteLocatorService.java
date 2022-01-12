package com.liem.apigateway.route;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
public class StaticRouteLocatorService implements RouteLocatorService {

    public static final String ROUTE_CONFIG_BASE_URL = "domain.base-url";

    public static final String TENANT_HEADER_NAME = "tenantId";

    public static final String ROUTE_CONFIG_TENANT_ID = "domain.tenant.id";

    private final Environment env;

    public StaticRouteLocatorService(Environment env) {
        this.env = env;
    }

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
        return this.env.getProperty(ROUTE_CONFIG_TENANT_ID, String.class);
    }

    private GatewayFilterSpec addTenantIdToHeader(GatewayFilterSpec f) {
        return f.addRequestHeader(TENANT_HEADER_NAME, getTenantId());
    }

    @Override
    public Buildable<Route> buildRoute(
            @NotNull PredicateSpec p, @NotBlank final String domain) {
        return p.path(getPath(domain))
                .filters(this::addTenantIdToHeader)
                .uri(getUri(domain));
    }


}
