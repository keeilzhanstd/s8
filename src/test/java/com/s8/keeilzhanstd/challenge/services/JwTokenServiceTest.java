package com.s8.keeilzhanstd.challenge.services;
import com.s8.keeilzhanstd.challenge.models.user.Role;
import com.s8.keeilzhanstd.challenge.models.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwTokenServiceTest extends BaseServiceTest {
    private final String username = "test";

    @Test
    void generateToken() {
        User user = new User(1L, username, "testpass", Role.USER);
        String token = jwTokenService.generateToken(user);
        assertEquals(username, jwTokenService.extractUsername(token));
    }

    @Test
    void isTokenValid() {
        User user = new User(1L, username, "testpass", Role.USER);
        String token = jwTokenService.generateToken(user);
        boolean actual = jwTokenService.isTokenValid(token, user);
        assertTrue(actual);
    }

    @Test
    void extractUsername() {
        User user = new User(1L, username, "testpass", Role.USER);
        String token = jwTokenService.generateToken(user);
        assertEquals(username, jwTokenService.extractUsername(token));
    }

}