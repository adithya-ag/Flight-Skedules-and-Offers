package com.example.apiTest.Configeration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AmadeusConfig {

    @Value("${amadeus.api.client-id}")
    private String clientId;

    @Value("${amadeus.api.client-secret}")
    private String clientSecret;

    @Value("${amadeus.api.url}")
    private String apiUrl;

    @Bean
    public String getClientId() {
        return clientId;
    }

    @Bean
    public String getClientSecret() {
        return clientSecret;
    }

    @Bean
    public String getApiUrl() {
        return apiUrl;
    }
}
