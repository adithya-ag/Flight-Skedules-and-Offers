package com.example.apiTest.Controller;

import com.example.apiTest.Service.AmadeusService;
import com.example.apiTest.Service.AuthenticationService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class AmadeusController {

    @Autowired
    private AmadeusService amadeusService;

    @GetMapping("/data/{Keyword}")
    public ResponseEntity<String> getData(
            @PathVariable String Keyword) {
        String endpoint = String.format("/v1/reference-data/locations?subType=AIRPORT&keyword=%s", Keyword);
        String data = amadeusService.getAmadeusData(endpoint);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/flight-status")
    public ResponseEntity<String> getFlightStatus(
            @RequestParam String carrierCode,
            @RequestParam String flightNumber,
            @RequestParam String scheduledDepartureDate) {
        String data = amadeusService.getFlightStatus(carrierCode, flightNumber, scheduledDepartureDate);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}

