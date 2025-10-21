package com.referencedpaymentsapi.model.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.referencedpaymentsapi.enums.PaymentStatus;
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
    private Long id;

    @Column(nullable = false, unique = true)
    private String referenceNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


}
