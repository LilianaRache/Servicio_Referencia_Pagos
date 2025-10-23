package com.referencedpaymentsapi.controller;


import com.referencedpaymentsapi.model.dto.ApiResponse;
import com.referencedpaymentsapi.model.dto.AuthRequest;
import com.referencedpaymentsapi.model.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.referencedpaymentsapi.security.JwtTokenUtil;
import com.referencedpaymentsapi.service.JwtUserDetailsService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class AuthController {

    /**
     * Endpoint para autenticar usuarios y generar un token JWT.
     * @param authRequest contiene el usuario y contraseña.
     * @return el token JWT si las credenciales son válidas.
     */

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;


    public AuthController(AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthResponse>> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Credenciales inválidas", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);


        AuthResponse response = new AuthResponse(token, String.valueOf(LocalDateTime.now()));

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                "200", "Autenticación exitosa", response );
        return ResponseEntity.ok(apiResponse);
    }


}
