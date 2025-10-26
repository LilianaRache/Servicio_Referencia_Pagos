package com.referencedpaymentsapi.service;

import com.referencedpaymentsapi.enums.PaymentStatus;
import com.referencedpaymentsapi.model.dto.PaymentCancelRequest;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.repository.PaymentReferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PaymentReferenceServiceTest {

    private PaymentReferenceRepository repository;
    private PaymentReferenceService service;

    private PaymentReference entity;

    @BeforeEach
    void setup() {
        repository = mock(PaymentReferenceRepository.class);
        service = new PaymentReferenceService(repository);

        entity = new PaymentReference();
        entity.setPaymentId(1L);
        entity.setAmount(new BigDecimal("100.00"));
        entity.setDescription("Pago de prueba");
        entity.setReference("REF1234567890ABCDEFGHIJKLM");
        entity.setDueDate(LocalDateTime.of(2024, 10, 30, 12, 0));
        entity.setCreationDate(LocalDateTime.of(2024, 10, 22, 10, 0));
        entity.setStatus(PaymentStatus.CREATED.getCode());
        entity.setCallBackURL("https://callback.test");
    }

    @Test
    void create_shouldSaveAndGenerateReference() {
        when(repository.save(any(PaymentReference.class))).thenReturn(entity);

        PaymentReference saved = service.create(entity);

        assertNotNull(saved);
        assertEquals(entity.getAmount(), saved.getAmount());
        assertEquals(entity.getDescription(), saved.getDescription());
        assertEquals(entity.getCallBackURL(), saved.getCallBackURL());
        assertNotNull(saved.getReference());
        assertEquals(30, saved.getReference().length());
        verify(repository, times(1)).save(entity);
    }

    @Test
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(Arrays.asList(entity));

        List<PaymentReference> list = service.findAll();

        assertEquals(1, list.size());
        assertEquals(entity.getPaymentId(), list.get(0).getPaymentId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findByPaymentIdAndReference_shouldReturnOptional() {
        when(repository.findByPaymentIdAndReference(1L, "REF1234567890ABCDEFGHIJKLM"))
                .thenReturn(Optional.of(entity));

        Optional<PaymentReference> result = service.findByPaymentIdAndReference("REF1234567890ABCDEFGHIJKLM", 1L);

        assertTrue(result.isPresent());
        assertEquals(entity.getPaymentId(), result.get().getPaymentId());
        verify(repository, times(1)).findByPaymentIdAndReference(1L, "REF1234567890ABCDEFGHIJKLM");
    }

    @Test
    void findByCreationDate_shouldReturnList() {
        LocalDateTime start = LocalDateTime.of(2024, 10, 21, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 10, 31, 23, 59);
        when(repository.findByCreationDateBetweenAndStatus(start, end, entity.getStatus())).thenReturn(Arrays.asList(entity));

        List<PaymentReference> list = service.findByCreationDateBetweenAndStatus(start, end, entity.getStatus());

        assertEquals(1, list.size());
        verify(repository, times(1)).findByCreationDateBetweenAndStatus(start, end, entity.getStatus());
    }

    @Test
    void findById_shouldReturnOptional() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<PaymentReference> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(entity.getPaymentId(), result.get().getPaymentId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void update_shouldCancelPayment() {
        PaymentCancelRequest request = new PaymentCancelRequest();
        request.setReference(entity.getReference());
        request.setStatus("03");
        request.setUpdateDescription("Pago cancelado");

        when(repository.findByReference(entity.getReference())).thenReturn(Optional.of(entity));
        when(repository.save(any(PaymentReference.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentReference updated = service.update(request);

        assertEquals(PaymentStatus.CANCELED.getCode(), updated.getStatus());
        assertEquals("Pago cancelado", updated.getCancelDescription());
        verify(repository, times(1)).findByReference(entity.getReference());
        verify(repository, times(1)).save(entity);
    }


    @Test
    void update_shouldThrowIfStatusNotCreated() {
        entity.setStatus(PaymentStatus.PAID.getCode());
        PaymentCancelRequest request = new PaymentCancelRequest();
        request.setReference(entity.getReference());
        request.setStatus("03");
        request.setUpdateDescription("Intento cancelación");

        when(repository.findByReference(entity.getReference())).thenReturn(Optional.of(entity));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.update(request));
        assertEquals("Solo se puede cancelar un pago con estado '01' (Created)", ex.getMessage());
    }

    @Test
    void update_shouldThrowIfReferenceNotFound() {
        PaymentCancelRequest request = new PaymentCancelRequest();
        request.setReference("NO_EXISTE");
        request.setStatus("03");
        request.setUpdateDescription("Cancelación");

        when(repository.findByReference("NO_EXISTE")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.update(request));
        assertEquals("Referencia no encontrada", ex.getMessage());
    }
}
