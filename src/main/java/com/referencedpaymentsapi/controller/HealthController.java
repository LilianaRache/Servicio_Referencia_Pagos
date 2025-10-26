package com.referencedpaymentsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Verifica el estado de salud del servicio.
 *
 * <p>Este endpoint permite comprobar si la API de Referenced Payments
 * se encuentra en ejecución y disponible para recibir solicitudes.</p>
 *
 * @return un mensaje de confirmación con el estado "OK" y el código HTTP 200.
 */

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
