package com.referencedpaymentsapi.model.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ApiResponse<T> {

    private String responseCode;
    private String responseMessage;
    private T data;

    public ApiResponse(String responseCode, String responseMessage, T data) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = data;
    }
}