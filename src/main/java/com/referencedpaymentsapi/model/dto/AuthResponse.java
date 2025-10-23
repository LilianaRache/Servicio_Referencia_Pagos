package com.referencedpaymentsapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashMap;


@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String CreateAt;

}
