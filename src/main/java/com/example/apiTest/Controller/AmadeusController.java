package com.example.apiTest.Controller;

import com.example.apiTest.Service.AmadeusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AmadeusController {

    @Autowired
    private AmadeusService amadeusService;

    @GetMapping("/data")
    public ResponseEntity<String> getData() {
        String endpoint = "/v1/reference-data/locations?subType=AIRPORT&keyword=BLR"; // Example endpoint and parameters
        String data = amadeusService.getAmadeusData(endpoint);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
