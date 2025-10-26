package com.referencedpaymentsapi.repository;

import com.referencedpaymentsapi.model.entity.PaymentReference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PaymentReferenceRepository extends JpaRepository<PaymentReference, Long> {

    Optional<PaymentReference> findByPaymentIdAndReference(Long paymentId, String reference);

    List<PaymentReference> findByCreationDateBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, String status);

    Optional<PaymentReference> findByReference(String reference);

}


