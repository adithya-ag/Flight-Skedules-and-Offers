package com.example.apiTest.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthenticationService {
    private final String clientId;
    private final String clientSecret;
    private String accessToken;
    private long expiryTime;
    private final String apiUrl;

    @Autowired
    public AuthenticationService(@Value("${amadeus.api.client-id}") String clientId,
                                 @Value("${amadeus.api.client-secret}") String clientSecret,
                                 @Value("${amadeus.api.url}") String apiUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.apiUrl = apiUrl;
        this.accessToken = null;
        this.expiryTime = 0;
    }

    public String getAccessToken() {
        System.out.printf("Access Token: %s%n", accessToken);
        if (accessToken == null || System.currentTimeMillis() > expiryTime) {
            refreshAccessToken();
        }
        return accessToken;
    }

    private void refreshAccessToken() {
        System.out.printf("Refreshing Access Token...%n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                apiUrl + "/v1/security/oauth2/token",
                entity,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null) {
            accessToken = (String) responseBody.get("access_token");
            Integer expiresIn = (Integer) responseBody.get("expires_in");
            expiryTime = System.currentTimeMillis() + (expiresIn * 1000);
        }
    }
}

