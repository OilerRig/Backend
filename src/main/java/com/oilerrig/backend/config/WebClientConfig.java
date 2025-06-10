package com.oilerrig.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        // You can add common configurations here, e.g., default headers, timeouts.
        return WebClient.builder();
    }
}