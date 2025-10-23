package com.referencedpaymentsapi.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentUpdateResponse {
    private Long paymentId;
    private LocalDateTime creationDate;

    private String reference;
    private String status;
    private String Message;
    private String cancelDescription;
    private LocalDateTime updatedAt;

}
