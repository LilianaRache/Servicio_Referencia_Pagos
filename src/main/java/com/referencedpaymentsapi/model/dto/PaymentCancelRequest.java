package com.referencedpaymentsapi.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCancelRequest {

    @NotBlank(message = "La referencia es obligatoria")
    private String reference;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "03", message = "El estado para cancelación debe ser '03'")
    private String status;

    @NotBlank(message = "La descripción de la cancelación es obligatoria")
    private String updateDescription;
}