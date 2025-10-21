package com.referencedpaymentsapi.repository;

import com.referencedpaymentsapi.model.entity.PaymentReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentReferenceRepository extends JpaRepository<PaymentReference, Long> {
    Optional<PaymentReference> findByReferenceNumber(String referenceNumber);
}


