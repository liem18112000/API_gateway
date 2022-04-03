package com.liem.apigateway.config;

import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static com.liem.apigateway.config.AppConfig.CIRCUIT_BREAKER_TIMEOUT;
import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.ofDefaults;

/**
 * Configuring Resilience4J Circuit Breakers
 * @see <a href="https://docs.spring.io/spring-cloud-circuitbreaker/docs/1.0.4.RELEASE/reference/html/#specific-circuit-breaker-configuration">Customize the Jackson ObjectMapper</a>
 */
@Configuration
public class CircuitBreakerConfig {

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(
                        Duration.ofSeconds(CIRCUIT_BREAKER_TIMEOUT)).build())
                .build());
    }
}
