package com.referencedpaymentsapi.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida que representa una referencia de pago enviada al cliente.
 */
@Getter
@Setter
public class PaymentReferenceResponse {
    private Long id;
    private String referenceNumber;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}
