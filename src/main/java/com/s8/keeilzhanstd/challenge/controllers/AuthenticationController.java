package com.s8.keeilzhanstd.challenge.controllers;

import com.s8.keeilzhanstd.challenge.annotations.Loggable;
import com.s8.keeilzhanstd.challenge.models.auth.AuthenticationRequest;
import com.s8.keeilzhanstd.challenge.models.auth.AuthenticationResponse;
import com.s8.keeilzhanstd.challenge.services.AuthenticationService;
import com.s8.keeilzhanstd.challenge.models.auth.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @Loggable
    @PostMapping("/register")
    @Operation(
            description = "Register a user and retrieve generated JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully registered"
                    )
            }
    )
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @Loggable
    @PostMapping("/authenticate")
    @Operation(
            description = "Authenticate user and retrieve generated JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully signed in"
                    )
            }
    )
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
