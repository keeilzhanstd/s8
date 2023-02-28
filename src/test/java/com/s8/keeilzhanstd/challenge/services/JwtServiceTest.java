package com.s8.keeilzhanstd.challenge.services;
import com.s8.keeilzhanstd.challenge.models.user.Role;
import com.s8.keeilzhanstd.challenge.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final String username = "test";
    @Autowired
    private JwtService service;

    @BeforeEach
    void setUp() {
        service = new JwtService();
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