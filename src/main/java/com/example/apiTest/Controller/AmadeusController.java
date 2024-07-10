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

    @GetMapping("/")
    public ResponseEntity<String> getRoot() {
        return new ResponseEntity<>("Welcome to the Amadeus API MicroService By Adithya G.A!!", HttpStatus.OK);
    }

    @GetMapping("/data/{subType}/{keyword}")
    public ResponseEntity<String> getData(
            @PathVariable("subType") String subType,
            @PathVariable("keyword") String keyword) {
        System.out.printf("SubType: %s, Keyword: %s%n", subType, keyword);
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
            @RequestParam String departureTime) {
        System.out.printf("entered flight availability controller%n");
        String data = amadeusService.getFlightAvailability(originLocationCode, destinationLocationCode, departureDate, departureTime);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}

