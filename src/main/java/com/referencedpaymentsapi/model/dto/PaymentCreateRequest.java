package com.referencedpaymentsapi.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO de entrada para crear o actualizar una referencia de pago.
 */
@Getter
@Setter
public class PaymentCreateRequest {

    @NotBlank(message = "El externalId es obligatorio")
    private String externalId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor que cero")
    private BigDecimal amount;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;

    @NotBlank(message = "La fecha de vencimiento (dueDate) es obligatoria")
    private String dueDate;

    @NotBlank(message = "El callbackURL es obligatorio")
    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "El callbackURL debe ser una URL válida")
    private String callbackURL;
}