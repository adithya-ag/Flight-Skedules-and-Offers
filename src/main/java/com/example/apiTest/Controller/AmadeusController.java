package com.example.apiTest.Controller;

import com.example.apiTest.Service.AmadeusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AmadeusController {

    @Autowired
    private AmadeusService amadeusService;

    @GetMapping("/data/{subType}/{keyword}")
    public ResponseEntity<String> getData(
            @PathVariable("subType") String subType,
            @PathVariable("keyword") String keyword) {

        String endpoint = String.format("/v1/reference-data/locations?subType=%s&keyword=%s", subType, keyword.toUpperCase());
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

    @GetMapping("/flight-availability")
    public ResponseEntity<String> getFlightAvailability(
            @RequestParam String originLocationCode,
            @RequestParam String destinationLocationCode,
            @RequestParam String departureDate,
            @RequestParam String travelerType,
            @RequestParam(required = false) String departureTime,
            @RequestParam(defaultValue = "GDS") String sources) {

        String requestBody = String.format(
                "{ \"originDestinations\": [ { \"id\": \"1\", \"originLocationCode\": \"%s\", \"destinationLocationCode\": \"%s\", \"departureDateTime\": { \"date\": \"%s\"%s } } ], \"travelers\": [ { \"id\": \"1\", \"travelerType\": \"%s\" } ], \"sources\": [ \"%s\" ] }",
                originLocationCode, destinationLocationCode, departureDate,
                (departureTime != null ? ", \"time\": \"" + departureTime + "\"" : ""),
                travelerType, sources);

        String data = amadeusService.getFlightAvailability(requestBody);
        return ResponseEntity.ok(data);
    }
}

