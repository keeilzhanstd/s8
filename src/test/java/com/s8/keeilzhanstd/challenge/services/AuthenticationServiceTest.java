package com.s8.keeilzhanstd.challenge.services;

import com.s8.keeilzhanstd.challenge.models.auth.AuthenticationRequest;
import com.s8.keeilzhanstd.challenge.models.auth.AuthenticationResponse;
import com.s8.keeilzhanstd.challenge.models.auth.RegisterRequest;
import com.s8.keeilzhanstd.challenge.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationServiceTest extends BaseServiceTest {

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void register() {
        String username = "test";
        AuthenticationResponse resp = authenticationService.register(new RegisterRequest(username, "test"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var token = resp.getToken();
        assertEquals(user.getUsername(), jwTokenService.extractUsername(token));
    }

    @Test
    void authenticate() {
        String username = "test";
        authenticationService.register(new RegisterRequest(username, "test"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        AuthenticationResponse respAuth = authenticationService.authenticate(new AuthenticationRequest(username, "test"));
        var token = respAuth.getToken();
        assertEquals(user.getUsername(), jwTokenService.extractUsername(token));
    }
}