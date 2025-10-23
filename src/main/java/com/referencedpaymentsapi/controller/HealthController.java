package com.referencedpaymentsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/")
public class HealthController {

    @GetMapping("health")
    public ResponseEntity<String> healthCheck() {

        return ResponseEntity
                .ok() //
                .body("API funcionando.");
    }
}
