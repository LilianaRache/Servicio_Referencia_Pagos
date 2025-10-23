package com.referencedpaymentsapi.controller;


import com.referencedpaymentsapi.mapper.PaymentReferenceMapper;
import com.referencedpaymentsapi.model.dto.*;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.service.PaymentReferenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las referencias de pago.
 */
@RestController
@RequestMapping("/v1/")
public class PaymentReferenceController {

    private final PaymentReferenceService service;
    private final PaymentReferenceMapper mapper;

    public PaymentReferenceController(PaymentReferenceService service, PaymentReferenceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @RequestMapping("/payment")
    public ResponseEntity<ApiResponse<PaymentCreateResponse>> create(@Valid @RequestBody PaymentCreateRequest request) {
        PaymentReference entity = mapper.toEntity(request);
        PaymentReference saved = service.create(entity);
        PaymentCreateResponse response = mapper.toResponseCreate(saved);
        ApiResponse<PaymentCreateResponse> apiResponse = new ApiResponse<>(
                "201", "Payment created successfully", response);
        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping("/payment/{reference}/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentReferenceResponse>> findById(@PathVariable String reference, Long paymentId ) {
        PaymentReferenceResponse paymentReference = service.findByPaymentIdAndReference(reference,paymentId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("PaymentReference not found"));

        ApiResponse<PaymentReferenceResponse> response = new ApiResponse<>(
                "200", "Payment verified successfully", paymentReference);

        return ResponseEntity.ok(response);
    }


    @GetMapping
    @RequestMapping("/payments/search")
    public ResponseEntity<ApiResponse<List<PaymentReferenceResponse>>> findAll(
            @RequestParam("startCreationDate") String startCreationDate,
            @RequestParam("endCreationDate") String endCreationDate){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startCreationDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endCreationDate, formatter);

        List<PaymentReferenceResponse> paymentReferences = service.findByCreationDate(start, end).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        ApiResponse<List<PaymentReferenceResponse>> response = new ApiResponse<>(
                "200", "Payments retrieved successfully", paymentReferences);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/payment/cancel")
    public ResponseEntity<ApiResponse<PaymentUpdateResponse>> update( @Valid @RequestBody PaymentCancelRequest request) {

        PaymentReference updatedEntity = service.update(request);
        PaymentUpdateResponse updatedResponse = mapper.toResponseUpdate(updatedEntity);

        ApiResponse<PaymentUpdateResponse> response = new ApiResponse<>(
                "200",
                "Referencia de pago actualizada exitosamente",
                updatedResponse
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        PaymentReference reference = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Referencia no encontrada"));

        // 🧾 Generar contenido del PDF (aquí simulado con texto simple)
        String pdfContent = "Comprobante de Pago\n\n" +
                "Referencia: " + reference.getReference() + "\n" +
                "Monto: " + reference.getAmount() + "\n" +
                "Descripción: " + reference.getDescription() + "\n" +
                "Estado: " + reference.getStatus() + "\n" +
                "Fecha de vencimiento: " + reference.getDueDate() + "\n";


        // Convertir a bytes (luego se puede reemplazar con un PDF real)
        byte[] pdfBytes = pdfContent.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "comprobante_" + reference.getPaymentId() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}