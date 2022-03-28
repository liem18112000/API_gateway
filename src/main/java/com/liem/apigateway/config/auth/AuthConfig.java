package com.liem.apigateway.config.auth;

import java.util.Set;

public class AuthConfig {
    public static final String HEADER_API_KEY = "x-api-key";
    public static final String HEADER_API_PRINCIPAL = "x-api-principal";

    public static final Set<String> EXCLUDED_AUTHORIZE_PATH = Set.of("/actuator/health");
}
