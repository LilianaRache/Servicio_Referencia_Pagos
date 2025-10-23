package com.referencedpaymentsapi.mapper;

import com.referencedpaymentsapi.enums.PaymentStatus;
import com.referencedpaymentsapi.model.dto.*;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentReferenceMapperTest {

    private PaymentReferenceMapper mapper;
    private LocalDateTime fixedDate;

    @BeforeEach
    void setup() {
        mapper = new PaymentReferenceMapper();
        fixedDate = LocalDateTime.of(2024, 10, 21, 12, 0);
    }

    @Test
    void toEntity_shouldMapRequestToEntity() {
        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setAmount(new BigDecimal("1500.00"));
        request.setDescription("Pago de prueba");
        request.setDueDate(fixedDate.toString());
        request.setCallbackURL("https://myurl/callback");

        PaymentReference entity = mapper.toEntity(request);

        assertThat(entity.getAmount()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(entity.getDescription()).isEqualTo("Pago de prueba");
        assertThat(entity.getDueDate()).isEqualTo(fixedDate);
        assertThat(entity.getCallBackURL()).isEqualTo("https://myurl/callback");
        assertThat(entity.getStatus()).isEqualTo(PaymentStatus.CREATED.getCode());
        assertThat(entity.getCreationDate()).isNotNull();
    }

    @Test
    void toResponseCreate_shouldMapEntityToCreateResponse() {
        PaymentReference entity = new PaymentReference();
        entity.setPaymentId(1L);
        entity.setReference("REF12345");
        entity.setAmount(new BigDecimal("1500.00"));
        entity.setDescription("Pago de prueba");
        entity.setStatus(PaymentStatus.CREATED.getCode());
        entity.setCreationDate(fixedDate);

        PaymentCreateResponse response = mapper.toResponseCreate(entity);

        assertThat(response.getPaymentId()).isEqualTo(1L);
        assertThat(response.getReference()).isEqualTo("REF12345");
        assertThat(response.getAmount()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(response.getDescription()).isEqualTo("Pago de prueba");
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.CREATED.getCode());
        assertThat(response.getMessage()).isEqualTo("Payment created successfully");
        assertThat(response.getCreationDate()).isNotNull();
    }

    @Test
    void toResponse_shouldMapEntityToResponse() {
        PaymentReference entity = new PaymentReference();
        entity.setPaymentId(1L);
        entity.setReference("REF12345");
        entity.setAmount(new BigDecimal("1500.00"));
        entity.setDescription("Pago de prueba");
        entity.setDueDate(fixedDate.plusDays(10));
        entity.setCallBackURL("https://myurl/callback");
        entity.setCallbackACKID("ACK123");
        entity.setCancelDescription("Cancelado");
        entity.setAuthorizationNumber("AUTH123");
        entity.setPaymentDate(fixedDate.plusDays(1));
        entity.setStatus(PaymentStatus.PAID.getCode());

        PaymentReferenceResponse response = mapper.toResponse(entity);

        assertThat(response.getPaymentId()).isEqualTo(1L);
        assertThat(response.getReference()).isEqualTo("REF12345");
        assertThat(response.getAmount()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(response.getDescription()).isEqualTo("Pago de prueba");
        assertThat(response.getDueDate()).isEqualTo(fixedDate.plusDays(10));
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PAID.getCode());
        assertThat(response.getCallBackURL()).isEqualTo("https://myurl/callback");
        assertThat(response.getCallbackACKID()).isEqualTo("ACK123");
        assertThat(response.getCancelDescription()).isEqualTo("Cancelado");
        assertThat(response.getAuthorizationNumber()).isEqualTo("AUTH123");
        assertThat(response.getPaymentDate()).isEqualTo(fixedDate.plusDays(1));
    }

    @Test
    void toResponseUpdate_shouldMapEntityToUpdateResponse() {
        PaymentReference entity = new PaymentReference();
        entity.setPaymentId(1L);
        entity.setReference("REF12345");
        entity.setDescription("Cancelado por prueba");
        entity.setStatus(PaymentStatus.CANCELED.getCode());
        entity.setCancelDescription("Cancelado por prueba");
        entity.setCreationDate(fixedDate);

        PaymentUpdateResponse response = mapper.toResponseUpdate(entity);

        assertThat(response.getPaymentId()).isEqualTo(1L);
        assertThat(response.getReference()).isEqualTo("REF12345");
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.CANCELED.getCode());
        assertThat(response.getCancelDescription()).isEqualTo("Cancelado por prueba");
        assertThat(response.getMessage()).isEqualTo("Payment updated successfully");
        assertThat(response.getCreationDate()).isEqualTo(fixedDate);
        assertThat(response.getUpdatedAt()).isNotNull();
    }
}