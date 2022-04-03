package com.liem.apigateway.config.auth;

import org.springframework.http.HttpMethod;

import java.util.Set;

public class AuthConfig {
    public static final Set<String> EXCLUDED_AUTHORIZE_PATH = Set.of("/actuator/health");
    public static final Set<HttpMethod> AUTHORIZATION_HTTP_METHODS = Set.of(
            HttpMethod.DELETE,
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.PATCH
    );
}
