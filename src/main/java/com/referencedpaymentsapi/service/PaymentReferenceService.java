package com.referencedpaymentsapi.service;

import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.repository.PaymentReferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio que gestiona la lógica de negocio para las referencias de pago.
 */
@Service
public class PaymentReferenceService {

    private final PaymentReferenceRepository paymentReferenceRepository;

    public PaymentReferenceService(PaymentReferenceRepository paymentReferenceRepository) {
        this.paymentReferenceRepository = paymentReferenceRepository;
    }

    /**
     * Crea una nueva referencia de pago con un número único.
     */
    public PaymentReference create(PaymentReference reference) {
        reference.setReferenceNumber(generateReferenceNumber());
        return paymentReferenceRepository.save(reference);
    }

    public List<PaymentReference> findAll() {
        return paymentReferenceRepository.findAll();
    }

    public Optional<PaymentReference> findById(Long id) {
        return paymentReferenceRepository.findById(id);
    }

    public PaymentReference update(Long id, PaymentReference updatedReference) {
        return paymentReferenceRepository.findById(id)
                .map(existing -> {
                    existing.setAmount(updatedReference.getAmount());
                    existing.setCurrency(updatedReference.getCurrency());
                    existing.setDescription(updatedReference.getDescription());
                    existing.setStatus(updatedReference.getStatus());
                    return paymentReferenceRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Referencia no encontrada"));
    }

    public void delete(Long id) {
        paymentReferenceRepository.deleteById(id);
    }

    private String generateReferenceNumber() {
        return "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}