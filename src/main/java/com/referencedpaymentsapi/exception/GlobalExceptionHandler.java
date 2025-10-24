package com.referencedpaymentsapi.exception;

import com.referencedpaymentsapi.model.dto.ApiResponse;
import com.referencedpaymentsapi.enums.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Maneja de forma global todas las excepciones de la aplicación.
 * Devuelve respuestas claras y estructuradas.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación (DTOs con @Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName, error.getDefaultMessage());
        });

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage(),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja errores de negocio (por ejemplo IllegalArgumentException)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                ResponseCode.CREATION_FAILED.getCode(),
                ex.getMessage(), // mensaje específico del negocio
                null
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    /**
     * Maneja errores inesperados
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex, WebRequest request) {
        ApiResponse<Object> response = new ApiResponse<>(
                ResponseCode.INTERNAL_ERROR.getCode(),
                ResponseCode.INTERNAL_ERROR.getMessage() + ": " + ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
