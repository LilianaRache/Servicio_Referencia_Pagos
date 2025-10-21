package com.referencedpaymentsapi.controller;


import com.referencedpaymentsapi.mapper.PaymentReferenceMapper;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.model.dto.PaymentReferenceRequest;
import com.referencedpaymentsapi.model.dto.PaymentReferenceResponse;
import com.referencedpaymentsapi.service.PaymentReferenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las referencias de pago.
 */
@RestController
@RequestMapping("/referencedpayments/references")
public class PaymentReferenceController {

    private final PaymentReferenceService service;
    private final PaymentReferenceMapper mapper;

    public PaymentReferenceController(PaymentReferenceService service, PaymentReferenceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PaymentReferenceResponse> create(@Valid @RequestBody PaymentReferenceRequest request) {
        PaymentReference entity = mapper.toEntity(request);
        PaymentReference saved = service.create(entity);
        return ResponseEntity.ok(mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<PaymentReferenceResponse>> findAll() {
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(mapper::toResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentReferenceResponse> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentReferenceResponse> update(@PathVariable Long id, @Valid @RequestBody PaymentReferenceRequest request) {
        PaymentReference updated = service.update(id, mapper.toEntity(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}