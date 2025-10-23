package com.referencedpaymentsapi.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentCancelResponse {

    private Long paymentId;
    private LocalDateTime creationDate;
    private String reference;
    private String status;
    private String message;
    private String cancelDescription;
    private LocalDateTime updatedAt;
}