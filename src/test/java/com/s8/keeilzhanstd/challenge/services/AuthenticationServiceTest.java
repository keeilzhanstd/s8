package com.s8.keeilzhanstd.challenge.services;

import com.s8.keeilzhanstd.challenge.config.JwTokenService;
import com.s8.keeilzhanstd.challenge.models.auth.AuthenticationRequest;
import com.s8.keeilzhanstd.challenge.models.auth.AuthenticationResponse;
import com.s8.keeilzhanstd.challenge.models.auth.RegisterRequest;
import com.s8.keeilzhanstd.challenge.models.user.User;
import com.s8.keeilzhanstd.challenge.models.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuthenticationServiceTest {

    @Autowired
    private UserRepository repository;
    private AuthenticationService service;
    private JwTokenService jwTokenService;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        jwTokenService = new JwTokenService();
        service = new AuthenticationService(repository, passwordEncoder, jwTokenService, new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return null;
            }
        });
    }


    @Test
    void register() {
        String username = "test";
        AuthenticationResponse resp = service.register(new RegisterRequest(username, "test"));
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var token = resp.getToken();
        assertEquals(user.getUsername(), jwTokenService.extractUsername(token));
    }

    @Test
    void authenticate() {
        String username = "test";
        service.register(new RegisterRequest(username, "test"));
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        AuthenticationResponse respAuth = service.authenticate(new AuthenticationRequest(username, "test"));
        var token = respAuth.getToken();
        assertEquals(user.getUsername(), jwTokenService.extractUsername(token));
    }
}