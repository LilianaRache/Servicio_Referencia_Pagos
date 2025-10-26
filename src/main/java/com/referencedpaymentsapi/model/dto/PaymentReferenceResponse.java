package com.referencedpaymentsapi.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentReferenceResponse {

    private Long paymentId;
    private BigDecimal amount;
    private String reference;
    private String description;
    private LocalDateTime dueDate;
    private String status;
    private String callBackURL;
    private String callbackACKID;
    private String cancelDescription;
    private String authorizationNumber;
    private LocalDateTime paymentDate;
}
