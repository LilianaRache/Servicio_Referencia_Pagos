package com.referencedpaymentsapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.referencedpaymentsapi.enums.PaymentStatus;
import com.referencedpaymentsapi.mapper.PaymentReferenceMapper;
import com.referencedpaymentsapi.model.dto.*;
import com.referencedpaymentsapi.model.entity.PaymentReference;
import com.referencedpaymentsapi.service.PaymentReferenceService;
import org.hamcrest.Matchers;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentReferenceControllerTest {

    private MockMvc mockMvc;

    private PaymentReferenceService service = Mockito.mock(PaymentReferenceService.class);
    private PaymentReferenceMapper mapper = Mockito.mock(PaymentReferenceMapper.class);

    @InjectMocks
    private PaymentReferenceController controller;

    private ObjectMapper objectMapper;

    private PaymentReference entity;
    private PaymentCreateRequest createRequest;
    private PaymentCreateResponse createResponse;

    private LocalDateTime fixedDate = LocalDateTime.of(2024, 10, 21, 12, 0, 0);

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        entity = new PaymentReference();
        entity.setPaymentId(1L);
        entity.setReference("REF12345");
        entity.setAmount(new BigDecimal("1500.00"));
        entity.setDescription("Pago de prueba");
        entity.setDueDate(fixedDate.plusDays(10));
        entity.setCreationDate(fixedDate);
        entity.setStatus(PaymentStatus.CREATED.getCode());
        entity.setCallBackURL("https://myurl/callback");
        entity.setCallbackACKID("");
        entity.setCancelDescription("");
        entity.setAuthorizationNumber("");
        entity.setPaymentDate(null);

        createRequest = new PaymentCreateRequest();
        createRequest.setExternalId("EXT12345");
        createRequest.setAmount(new BigDecimal("1500.00"));
        createRequest.setDescription("Pago de prueba");
        createRequest.setDueDate(fixedDate.plusDays(10).toString());
        createRequest.setCallbackURL("https://myurl/callback");

        createResponse = new PaymentCreateResponse();
        createResponse.setPaymentId(1L);
        createResponse.setReference("REF12345");
        createResponse.setAmount(new BigDecimal("1500.00"));
        createResponse.setDescription("Pago de prueba");
        createResponse.setCreationDate(fixedDate);
        createResponse.setStatus(PaymentStatus.CREATED.getCode());
        createResponse.setMessage("Payment created successfully");
    }

    @Test
    void create_shouldReturn201() throws Exception {
        Mockito.when(mapper.toEntity(any(PaymentCreateRequest.class))).thenReturn(entity);
        Mockito.when(service.create(any(PaymentReference.class))).thenReturn(entity);
        Mockito.when(mapper.toResponseCreate(any(PaymentReference.class))).thenReturn(createResponse);

        mockMvc.perform(post("/v1/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value("201"))
                .andExpect(jsonPath("$.responseMessage").value("Payment created successfully"))
                .andExpect(jsonPath("$.data.paymentId").value(1))
                .andExpect(jsonPath("$.data.reference").value("REF12345"))
                .andExpect(jsonPath("$.data.amount").value(1500.00))
                .andExpect(jsonPath("$.data.status").value("01"));
    }

    @Test
    void findById_shouldReturnOk() throws Exception {
        PaymentReferenceResponse response = new PaymentReferenceResponse();
        response.setPaymentId(1L);
        response.setReference("REF12345");
        response.setAmount(new BigDecimal("1500.00"));
        response.setDescription("Pago de prueba");
        response.setDueDate(fixedDate.plusDays(10));
        response.setStatus(PaymentStatus.CREATED.getCode());
        response.setCallBackURL("https://myurl/callback");

        Mockito.when(service.findByPaymentIdAndReference("REF12345", 1L)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        mockMvc.perform(get("/v1/payment/REF12345/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("200"))
                .andExpect(jsonPath("$.data.reference").value("REF12345"))
                .andExpect(jsonPath("$.data.amount").value(1500.00))
                .andExpect(jsonPath("$.data.status").value("01"));
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        Mockito.when(service.findByPaymentIdAndReference("REF12345", 99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/payment/REF12345/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseCode").value("404"))
                .andExpect(jsonPath("$.responseMessage").value("PaymentReference not found"));
    }

    @Test
    void findAll_shouldReturnList() throws Exception {
        PaymentReferenceResponse response = new PaymentReferenceResponse();
        response.setPaymentId(1L);
        response.setReference("REF12345");
        response.setAmount(new BigDecimal("1500.00"));
        response.setDescription("Pago de prueba");
        response.setDueDate(fixedDate.plusDays(10));
        response.setStatus(PaymentStatus.CREATED.getCode());

        List<PaymentReference> list = Arrays.asList(entity);
        Mockito.when(service.findByCreationDateBetweenAndStatus(fixedDate, fixedDate.plusDays(10), response.getStatus())).thenReturn(list);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        mockMvc.perform(get("/v1/payments/search")
                        .param("startCreationDate", fixedDate.format(formatter))
                        .param("endCreationDate", fixedDate.plusDays(10).format(formatter))
                        .param("status", "01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].reference").value("REF12345"))
                .andExpect(jsonPath("$.data[0].amount").value(1500.00))
                .andExpect(jsonPath("$.data[0].status").value("01"));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        PaymentCancelRequest cancelRequest = new PaymentCancelRequest();
        cancelRequest.setReference("REF12345");
        cancelRequest.setStatus("03");
        cancelRequest.setUpdateDescription("Cancelado por prueba");

        PaymentReference updatedEntity = new PaymentReference();
        updatedEntity.setPaymentId(1L);
        updatedEntity.setReference("REF12345");
        updatedEntity.setAmount(new BigDecimal("1500.00"));
        updatedEntity.setDescription("Cancelado por prueba");
        updatedEntity.setCreationDate(fixedDate);
        updatedEntity.setStatus(PaymentStatus.CANCELED.getCode());

        PaymentUpdateResponse updateResponse = new PaymentUpdateResponse();
        updateResponse.setPaymentId(1L);
        updateResponse.setReference("REF12345");
        updateResponse.setStatus("03");
        updateResponse.setMessage("Payment updated successfully");
        updateResponse.setUpdatedAt(fixedDate);

        Mockito.when(service.update(any(PaymentCancelRequest.class))).thenReturn(updatedEntity);
        Mockito.when(mapper.toResponseUpdate(updatedEntity)).thenReturn(updateResponse);

        mockMvc.perform(put("/v1/payment/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reference").value("REF12345"))
                .andExpect(jsonPath("$.data.status").value("03"))
                .andExpect(jsonPath("$.data.message").value("Payment updated successfully"));
    }

    @Test
    void downloadPdf_shouldReturnPdfBytes() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/v1/payment/1/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"comprobante_1.pdf\""))
                .andExpect(content().contentType("application/pdf"));
    }
}