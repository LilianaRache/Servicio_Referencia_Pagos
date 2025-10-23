package com.referencedpaymentsapi.mapper;

import com.referencedpaymentsapi.enums.PaymentStatus;
import com.referencedpaymentsapi.model.dto.PaymentCreateResponse;
import com.referencedpaymentsapi.model.dto.PaymentReferenceResponse;
import com.referencedpaymentsapi.model.dto.PaymentUpdateResponse;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.model.dto.PaymentCreateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Clase de utilidad para convertir entre entidades y DTOs.
 */
@Component
public class PaymentReferenceMapper {

    public PaymentReference toEntity(PaymentCreateRequest request) {
        PaymentReference entity = new PaymentReference();
        entity.setAmount(request.getAmount());
        entity.setDescription(request.getDescription());
        entity.setDueDate(LocalDateTime.parse(request.getDueDate()));
        entity.setCreationDate(LocalDateTime.now());
        entity.setStatus(PaymentStatus.CREATED.getCode());
        entity.setCallBackURL(request.getCallbackURL());
        return entity;
    }

    public PaymentCreateResponse toResponseCreate(PaymentReference entity) {
        PaymentCreateResponse response = new PaymentCreateResponse();
        response.setPaymentId(entity.getPaymentId());
        response.setReference(entity.getReference());
        response.setAmount(entity.getAmount());
        response.setDescription(entity.getDescription());
        response.setCreationDate(LocalDateTime.now());
        response.setStatus(entity.getStatus());
        response.setMessage("Payment created successfully");
        return response;
    }

    public PaymentReferenceResponse toResponse(PaymentReference entity) {
        PaymentReferenceResponse response = new PaymentReferenceResponse();
        response.setPaymentId(entity.getPaymentId());
        response.setAmount(entity.getAmount());
        response.setReference(entity.getReference());
        response.setDescription(entity.getDescription());
        response.setDueDate(entity.getDueDate());
        response.setStatus(entity.getStatus());
        response.setCallBackURL(entity.getCallBackURL());
        response.setCallbackACKID(entity.getCallbackACKID());
        response.setCancelDescription(entity.getCancelDescription());
        response.setAuthorizationNumber(entity.getAuthorizationNumber());
        response.setPaymentDate(entity.getPaymentDate());
        return response;

    }

    public PaymentUpdateResponse toResponseUpdate(PaymentReference entity) {
        PaymentUpdateResponse response = new PaymentUpdateResponse();
        response.setPaymentId(entity.getPaymentId());
        response.setCreationDate(entity.getCreationDate());
        response.setReference(entity.getReference());
        response.setStatus(PaymentStatus.CANCELED.getCode());
        response.setMessage("Payment updated successfully");
        response.setCancelDescription(entity.getCancelDescription());
        response.setUpdatedAt(LocalDateTime.now());
        return response;
    }

}