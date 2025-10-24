package com.referencedpaymentsapi.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS("200", "Success"),
    CREATED("201", "Payment created successfully"),
    CANCELED("202", "Payment canceled successfully"),
    INVALID_REQUEST("400", "Invalid request"),
    UNAUTHORIZED("401", "Unauthorized"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Payment not found"),
    ALREADY_PROCESSED("409", "Payment already processed"),
    CREATION_FAILED("422", "Payment creation failed"),
    INTERNAL_ERROR("500", "Internal server error");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}