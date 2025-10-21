package com.referencedpaymentsapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.referencedpaymentsapi.enums.PaymentStatus;
import com.referencedpaymentsapi.mapper.PaymentReferenceMapper;
import com.referencedpaymentsapi.model.dto.PaymentReferenceRequest;
import com.referencedpaymentsapi.model.dto.PaymentReferenceResponse;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.service.PaymentReferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test unitario del controlador PaymentReferenceController.
 * Aísla completamente la lógica del controlador sin JWT ni filtros de seguridad.
 */
class PaymentReferenceControllerTest {

    private MockMvc mockMvc;

    PaymentReferenceService service = Mockito.mock(PaymentReferenceService.class);

    PaymentReferenceMapper mapper = Mockito.mock(PaymentReferenceMapper.class);

    @InjectMocks
    private PaymentReferenceController controller;

    private ObjectMapper objectMapper;

    private PaymentReference entity;
    private PaymentReferenceRequest request;
    private PaymentReferenceResponse response;

    @BeforeEach
    void setup() {

        objectMapper = new ObjectMapper();

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        entity = new PaymentReference();
        entity.setId(1L);
        entity.setReferenceNumber("REF12345");
        entity.setAmount(new BigDecimal("1500.00"));
        entity.setCurrency("USD");
        entity.setDescription("Pago de prueba");
        entity.setStatus(PaymentStatus.PENDING);
        entity.setCreatedAt(LocalDateTime.now());

        request = new PaymentReferenceRequest();
        request.setAmount(new BigDecimal("1500.00"));
        request.setCurrency("USD");
        request.setDescription("Pago de prueba");

        response = new PaymentReferenceResponse();
        response.setId(1L);
        response.setReferenceNumber("REF12345");
        response.setAmount(new BigDecimal("1500.00"));
        response.setCurrency("USD");
        response.setDescription("Pago de prueba");
        response.setStatus(String.valueOf(PaymentStatus.PENDING));
        response.setCreatedAt(entity.getCreatedAt());
    }

    @Test
    void create_shouldReturnOk() throws Exception {

        Mockito.when(mapper.toEntity(any(PaymentReferenceRequest.class))).thenReturn(entity);
        Mockito.when(service.create(any(PaymentReference.class))).thenReturn(entity);
        Mockito.when(mapper.toResponse(any(PaymentReference.class))).thenReturn(response);

        mockMvc.perform(post("/referencedpayments/references")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.referenceNumber").value("REF12345"))
                .andExpect(jsonPath("$.amount").value(1500.00))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void findAll_shouldReturnList() throws Exception {
        List<PaymentReference> list = Arrays.asList(entity);
        Mockito.when(service.findAll()).thenReturn(list);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        mockMvc.perform(get("/referencedpayments/references"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].referenceNumber").value("REF12345"))
                .andExpect(jsonPath("$[0].currency").value("USD"));
    }

    @Test
    void findById_shouldReturnOk() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        mockMvc.perform(get("/referencedpayments/references/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referenceNumber").value("REF12345"))
                .andExpect(jsonPath("$.amount").value(1500.00));
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        Mockito.when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/referencedpayments/references/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        Mockito.when(mapper.toEntity(any(PaymentReferenceRequest.class))).thenReturn(entity);
        Mockito.when(service.update(eq(1L), any(PaymentReference.class))).thenReturn(entity);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        mockMvc.perform(put("/referencedpayments/references/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.referenceNumber").value("REF12345"));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/referencedpayments/references/1"))
                .andExpect(status().isNoContent());
    }
}
