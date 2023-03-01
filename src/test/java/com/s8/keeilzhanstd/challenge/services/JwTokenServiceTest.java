package com.s8.keeilzhanstd.challenge.services;
import com.s8.keeilzhanstd.challenge.config.JwTokenService;
import com.s8.keeilzhanstd.challenge.models.user.Role;
import com.s8.keeilzhanstd.challenge.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class JwTokenServiceTest {
    private final String username = "test";
    @Autowired
    private JwTokenService service;

    @BeforeEach
    void setUp() {
        service = new JwTokenService();
    }

    @Test
    void generateToken() {
        User user = new User(1L, username, "testpass", Role.USER);
        String token = service.generateToken(user);
        assertEquals(username, service.extractUsername(token));
    }

    @Test
    void isTokenValid() {
        User user = new User(1L, username, "testpass", Role.USER);
        String token = service.generateToken(user);
        boolean actual = service.isTokenValid(token, user);
        assertTrue(actual);
    }

    @Test
    void extractUsername() {
        User user = new User(1L, username, "testpass", Role.USER);
        String token = service.generateToken(user);
        assertEquals(username, service.extractUsername(token));
    }

}