package com.s8.keeilzhanstd.challenge.controllers;

import com.s8.keeilzhanstd.challenge.annotations.Loggable;
import com.s8.keeilzhanstd.challenge.models.auth.AuthenticationRequest;
import com.s8.keeilzhanstd.challenge.models.auth.RegisterRequest;
import com.s8.keeilzhanstd.challenge.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
                            responseCode = "201",
                            description = "Successfully registered"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error message: e.getMessage()"
                    )
            }
    )
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Loggable
    @PostMapping("/authenticate")
    @Operation(
            description = "Authenticate user and retrieve generated JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully signed in"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error message: e.getMessage()"
                    )
            }
    )
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.authenticate(request));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
