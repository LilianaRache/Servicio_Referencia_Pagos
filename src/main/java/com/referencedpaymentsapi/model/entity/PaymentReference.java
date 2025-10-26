package com.referencedpaymentsapi.model.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa una referencia de pago en el sistema.
 */
@Entity
@Table(name = "payment_references")
@Getter
@Setter
public class PaymentReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private BigDecimal amount; // viene del front

    @Column(nullable = false, unique = true, length = 50)
    private String reference; // autogenerado

    @Column(nullable = false)
    private String description; // viene del front

    @Column(nullable = false)
    private LocalDateTime dueDate; // viene del front

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false, length = 2)
    private String status; // CREATED, PAID, CANCELED

    @Column(name = "callback_url", nullable = false)
    private String callBackURL; // viene del front

    private String callbackACKID = ""; // en blanco

    @Column(name = "cancel_description")
    private String cancelDescription = ""; // en blanco

    private String authorizationNumber = ""; // en blanco

    private LocalDateTime paymentDate; // en blanco al crearse

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }

}