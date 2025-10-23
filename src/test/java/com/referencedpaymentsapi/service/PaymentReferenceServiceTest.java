package com.referencedpaymentsapi.service;

import com.referencedpaymentsapi.enums.PaymentStatus;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.repository.PaymentReferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentReferenceServiceTest {

    @Mock
    private PaymentReferenceRepository repository;

    @InjectMocks
    private PaymentReferenceService service;

    private PaymentReference reference;

    @BeforeEach
    void setUp() {
        reference = new PaymentReference();
        reference.setId(1L);
        reference.setAmount(BigDecimal.valueOf(100));
        reference.setCurrency("USD");
        reference.setDescription("Test payment");
        reference.setStatus(PaymentStatus.CREATED);
    }

    @Test
    void testCreateShouldGenerateReferenceNumberAndSave() {
        when(repository.save(any(PaymentReference.class))).thenAnswer(i -> i.getArgument(0));

        PaymentReference saved = service.create(reference);

        assertNotNull(saved.getReferenceNumber());
        assertTrue(saved.getReferenceNumber().startsWith("REF-"));
        verify(repository, times(1)).save(any(PaymentReference.class));
    }

    @Test
    void testFindAllReturnsList() {
        when(repository.findAll()).thenReturn(List.of(reference));

        List<PaymentReference> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getCurrency());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(reference));

        Optional<PaymentReference> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("USD", result.get().getCurrency());
        verify(repository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<PaymentReference> result = service.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateExisting() {
        PaymentReference updated = new PaymentReference();
        updated.setAmount(BigDecimal.valueOf(200));
        updated.setDescription("Updated desc");
        updated.setStatus(PaymentStatus.CANCELED);

        when(repository.findById(1L)).thenReturn(Optional.of(reference));
        when(repository.save(any(PaymentReference.class))).thenAnswer(i -> i.getArgument(0));

        //TODO: Adjust the update
        PaymentReference result = service.update(1L, updated);

        assertEquals(BigDecimal.valueOf(200), result.getAmount());
        assertEquals("EUR", result.getCurrency());
        assertEquals(PaymentStatus.PAID, result.getStatus());
        verify(repository).save(reference);
    }

    @Test
    void testUpdateNotFoundThrowsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.update(99L, reference));
    }

    @Test
    void testDelete() {
        doNothing().when(repository).deleteById(1L);
        service.delete(1L);
        verify(repository).deleteById(1L);
    }
}
