package com.liem.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class ApIgatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApIgatewayApplication.class, args);
    }

}
