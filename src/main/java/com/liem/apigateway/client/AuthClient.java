package com.liem.apigateway.client;

import com.liem.apigateway.client.beans.AuthDTO;
import com.liem.apigateway.config.ApiV1;
import io.sentry.spring.tracing.SentrySpan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

import static com.liem.apigateway.config.AppConfig.*;

@ReactiveFeignClient(name = "${services.security-service.name}")
public interface AuthClient {

    @SentrySpan
    @PostMapping("endpoint/security/" + ApiV1.URI_API + "/authorize")
    Mono<AuthDTO> authorize(
            @RequestHeader(X_API_KEY_HEADER) String apiKey
    );

    @SentrySpan
    @PostMapping("endpoint/security/" + ApiV1.URI_API + "/authenticate")
    Mono<AuthDTO> authenticate(
            @RequestHeader(X_API_KEY_HEADER) String apiKey,
            @RequestHeader(X_PRINCIPAL_HEADER) String principal
    );
}
