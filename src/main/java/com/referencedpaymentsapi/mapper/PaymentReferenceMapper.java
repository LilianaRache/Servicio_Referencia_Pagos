package com.referencedpaymentsapi.mapper;

import com.referencedpaymentsapi.enums.PaymentStatus;
import com.referencedpaymentsapi.model.dto.PaymentReferenceResponse;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.model.dto.PaymentReferenceRequest;
import org.springframework.stereotype.Component;

/**
 * Clase de utilidad para convertir entre entidades y DTOs.
 */
@Component
public class PaymentReferenceMapper {

    public PaymentReference toEntity(PaymentReferenceRequest request) {
        PaymentReference entity = new PaymentReference();
        entity.setAmount(request.getAmount());
        entity.setCurrency(request.getCurrency());
        entity.setDescription(request.getDescription());
        entity.setStatus(PaymentStatus.PENDING);
        return entity;
    }

    public PaymentReferenceResponse toResponse(PaymentReference entity) {
        PaymentReferenceResponse response = new PaymentReferenceResponse();
        response.setId(entity.getId());
        response.setReferenceNumber(entity.getReferenceNumber());
        response.setAmount(entity.getAmount());
        response.setCurrency(entity.getCurrency());
        response.setDescription(entity.getDescription());
        response.setStatus(entity.getStatus().name());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}