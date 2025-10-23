package com.referencedpaymentsapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    CREATED("01"),
    PAID("02"),
    CANCELED("03"),
    EXPIRED("04");

    private final String code;

    public String getCode() {
        return code;
    }

    public static PaymentStatus fromCode(String code) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentStatus code: " + code);
    }
}
