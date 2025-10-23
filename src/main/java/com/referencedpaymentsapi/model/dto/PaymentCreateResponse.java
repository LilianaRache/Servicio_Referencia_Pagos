package com.referencedpaymentsapi.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida que representa una referencia de pago creada por el cliente.
 */
@Getter
@Setter
public class PaymentCreateResponse {
    private Long paymentId;
    private String reference;
    private BigDecimal amount;
    private String description;
    private LocalDateTime creationDate;
    private String status;
    private String Message;


}
