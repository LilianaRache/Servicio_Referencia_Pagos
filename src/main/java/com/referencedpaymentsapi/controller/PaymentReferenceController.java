package com.referencedpaymentsapi.controller;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.referencedpaymentsapi.mapper.PaymentReferenceMapper;
import com.referencedpaymentsapi.model.dto.*;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.service.PaymentReferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las referencias de pago.
 */

@Tag(name = "💳 Referencias de Pago", description = "Endpoints para crear, consultar, listar y cancelar referencias de pago.")
@RestController
@RequestMapping("/v1")
public class PaymentReferenceController {

    private final PaymentReferenceService service;
    private final PaymentReferenceMapper mapper;

    public PaymentReferenceController(PaymentReferenceService service, PaymentReferenceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Crear una nueva referencia de pago",
            description = "Crea una referencia de pago en el sistema con la información proporcionada en el cuerpo de la solicitud."
    )
    @PostMapping("/payment")
    public ResponseEntity<ApiResponse<PaymentCreateResponse>> create(@Valid @RequestBody PaymentCreateRequest request) {
        PaymentReference entity = mapper.toEntity(request);
        PaymentReference saved = service.create(entity);
        PaymentCreateResponse response = mapper.toResponseCreate(saved);
        ApiResponse<PaymentCreateResponse> apiResponse = new ApiResponse<>(
                "201", "Payment created successfully", response);
        return ResponseEntity.status(201).body(apiResponse);
    }

    @Operation(
            summary = "Consultar una referencia de pago",
            description = "Obtiene la información completa de una referencia de pago a partir de su referencia y ID de pago."
    )
    @GetMapping("/payment/{reference}/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentReferenceResponse>> findById(@PathVariable String reference, @PathVariable Long paymentId) {
        Optional<PaymentReferenceResponse> optResponse = service.findByPaymentIdAndReference(reference, paymentId)
                .map(mapper::toResponse);

        if (optResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("404", "PaymentReference not found", null));
        }

        ApiResponse<PaymentReferenceResponse> response = new ApiResponse<>(
                "200", "Payment verified successfully", optResponse.get());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar referencias de pago",
            description = "Devuelve todas las referencias de pago creadas entre dos fechas dadas y filtradas por estado."
    )
    @GetMapping("/payments/search")
    public ResponseEntity<ApiResponse<List<PaymentReferenceResponse>>> findAll(
            @RequestParam("startCreationDate") String startCreationDate,
            @RequestParam("endCreationDate") String endCreationDate,
            @RequestParam("status") String status) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startCreationDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endCreationDate, formatter);

        List<PaymentReferenceResponse> paymentReferences = service.findByCreationDateBetweenAndStatus(start, end, status).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        ApiResponse<List<PaymentReferenceResponse>> response = new ApiResponse<>(
                "200", "Payments retrieved successfully", paymentReferences);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cancelar una referencia de pago",
            description = "Actualiza el estado de una referencia de pago existente a 'Cancelado (03)'."
    )
    @PutMapping("/payment/cancel")
    public ResponseEntity<ApiResponse<PaymentUpdateResponse>> update(@Valid @RequestBody PaymentCancelRequest request) {

        PaymentReference updatedEntity = service.update(request);
        PaymentUpdateResponse updatedResponse = mapper.toResponseUpdate(updatedEntity);

        ApiResponse<PaymentUpdateResponse> response = new ApiResponse<>(
                "200",
                "Referencia de pago actualizada exitosamente",
                updatedResponse
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Descargar comprobante PDF",
            description = "Genera y descarga el comprobante de pago en formato PDF para una referencia específica."
    )
    @GetMapping("/payment/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) throws DocumentException {
        PaymentReference reference = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Referencia no encontrada"));

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Comprobante de Pago"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Referencia: " + reference.getReference()));
            document.add(new Paragraph("Monto: " + reference.getAmount()));
            document.add(new Paragraph("Descripción: " + reference.getDescription()));
            document.add(new Paragraph("Estado: " + reference.getStatus()));
            document.add(new Paragraph("Fecha de vencimiento: " + reference.getDueDate()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Gracias por usar nuestro servicio."));

            document.close();

            byte[] pdfBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"comprobante_" + reference.getPaymentId() + ".pdf\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (DocumentException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}