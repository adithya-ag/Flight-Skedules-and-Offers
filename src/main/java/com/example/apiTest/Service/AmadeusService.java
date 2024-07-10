package com.example.apiTest.Service;

import com.example.apiTest.Excepetions.AmadeusApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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

        try {
            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            // Parse the error response body
            Map<String, Object> errorResponse = restTemplate.getForObject(endpoint, Map.class);
            if (errorResponse != null && errorResponse.containsKey("errors")) {
                Map<String, Object> error = ((List<Map<String, Object>>) errorResponse.get("errors")).get(0);
                int statusCode = e.getStatusCode().value();
                int errorCode = (int) error.get("code");
                String errorTitle = (String) error.get("title");
                String errorDetail = (String) error.get("detail");
                throw new AmadeusApiException(statusCode, errorCode, errorTitle, errorDetail);
            }
            throw e; // Re-throw the original exception if parsing fails
        }
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

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl + endpoint, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            // Parse the error response body
            Map<String, Object> errorResponse = restTemplate.getForObject(endpoint, Map.class);
            if (errorResponse != null && errorResponse.containsKey("errors")) {
                Map<String, Object> error = ((List<Map<String, Object>>) errorResponse.get("errors")).get(0);
                int statusCode = e.getStatusCode().value();
                int errorCode = (int) error.get("code");
                String errorTitle = (String) error.get("title");
                String errorDetail = (String) error.get("detail");
                throw new AmadeusApiException(statusCode, errorCode, errorTitle, errorDetail);
            }
            throw e; // Re-throw the original exception if parsing fails
        }
    }



    public String getFlightAvailability(String originLocationCode, String destinationLocationCode, String departureDate, String departureTime) {
        String accessToken = authService.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        System.out.printf("Header: ", headers);
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/vnd.amadeus+json");

        String endpoint = apiUrl + "/v1/shopping/availability/flight-availabilities";

        String requestBody = String.format("{ \"originDestinations\": [{ \"id\": \"1\", \"originLocationCode\": \"%s\", \"destinationLocationCode\": \"%s\", \"departureDateTime\": { \"date\": \"%s\", \"time\": \"%s\" } }], \"travelers\": [{ \"id\": \"1\", \"travelerType\": \"ADULT\" }], \"sources\": [\"GDS\"] }",
                originLocationCode, destinationLocationCode, departureDate, departureTime);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            // Parse the error response body
            Map<String, Object> errorResponse = restTemplate.getForObject(endpoint, Map.class);
            if (errorResponse != null && errorResponse.containsKey("errors")) {
                Map<String, Object> error = ((List<Map<String, Object>>) errorResponse.get("errors")).get(0);
                int statusCode = e.getStatusCode().value();
                int errorCode = (int) error.get("code");
                String errorTitle = (String) error.get("title");
                String errorDetail = (String) error.get("detail");
                throw new AmadeusApiException(statusCode, errorCode, errorTitle, errorDetail);
            }
            throw e; // Re-throw the original exception if parsing fails
        }
    }
}

