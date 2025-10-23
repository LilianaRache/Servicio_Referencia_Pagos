package com.referencedpaymentsapi.service;

import com.referencedpaymentsapi.enums.PaymentStatus;
import com.referencedpaymentsapi.model.dto.PaymentCancelRequest;
import com.referencedpaymentsapi.model.dto.PaymentUpdateResponse;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.repository.PaymentReferenceRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        reference.setReference(generateReference());
        return paymentReferenceRepository.save(reference);
    }

    public List<PaymentReference> findAll() {
        return paymentReferenceRepository.findAll();
    }

    public Optional<PaymentReference> findByPaymentIdAndReference(String reference, Long id) {
        return paymentReferenceRepository.findByPaymentIdAndReference(id, reference);
    }

    public List<PaymentReference> findByCreationDate(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentReferenceRepository.findByCreationDateBetween(startDate, endDate);
    }

    public Optional<PaymentReference> findById(Long id) {
        return paymentReferenceRepository.findById(id);
    }

    public PaymentReference update(PaymentCancelRequest request) {

        PaymentReference existing = paymentReferenceRepository.findByReference(request.getReference())
                .orElseThrow(() -> new RuntimeException("Referencia no encontrada"));

        if (!existing.getStatus().equals(PaymentStatus.CREATED.getCode())) {
            throw new RuntimeException("Solo se puede cancelar un pago con estado '01' (Created)");
        }

        existing.setStatus(PaymentStatus.CANCELED.getCode());
        existing.setDescription(request.getUpdateDescription());

        return paymentReferenceRepository.save(existing);
    }
    

    /**
     * Genera una referencia de 30 caracteres exactos:
     * Ejemplo: 22102025081455999SKAS82HFY57SU
     */
    private String generateReference() {
        // 1️⃣ Timestamp con milisegundos → 17 caracteres
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS"));

        // 2️⃣ Parte aleatoria alfanumérica → 13 caracteres
        String randomPart = generateRandomAlphaNumeric(13);

        // 3️⃣ Concatenar y asegurar longitud 30
        String reference = timestamp + randomPart;
        return reference.length() > 30 ? reference.substring(0, 30) : reference;
    }

    /**
     * Genera una cadena alfanumérica segura de longitud n
     */
    private String generateRandomAlphaNumeric(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}