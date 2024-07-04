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
public class AmadeusService {

    private final RestTemplate restTemplate;
    private final AuthenticationService authService;
    private final String apiUrl;

    @Autowired
    public AmadeusService(RestTemplate restTemplate,
                          AuthenticationService authService,
                          @Value("${amadeus.api.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.apiUrl = apiUrl;
    }



    public String getFlightStatus(String carrierCode, String flightNumber, String scheduledDepartureDate) {
        String accessToken = authService.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        String endpoint = String.format(
                "%s/v2/schedule/flights?carrierCode=%s&flightNumber=%s&scheduledDepartureDate=%s",
                apiUrl, carrierCode, flightNumber, scheduledDepartureDate
        );

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }



//    public String getAccessToken() {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "client_credentials");
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(
//                apiUrl + "/v1/security/oauth2/token",
//                entity,
//                Map.class
//        );
//
//        Map<String, String> responseBody = response.getBody();
//        return responseBody != null ? responseBody.get("access_token") : null;
//    }

    public String getAmadeusData(String endpoint) {
//        String accessToken = getAccessToken();
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);

        String accessToken = authService.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl + endpoint,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}

