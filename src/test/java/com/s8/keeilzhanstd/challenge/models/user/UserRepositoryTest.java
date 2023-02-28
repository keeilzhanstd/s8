package com.s8.keeilzhanstd.challenge.models.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.ClassBasedNavigableIterableAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void findByUsername() {
        String username = "username";
        User user = new User(1000L, username, "password", Role.USER);

        repository.save(user);

        //when
        Optional<User> expected = repository.findByUsername(username);

        if (expected.isEmpty()) {
            assertEquals(expected, Optional.empty());
        } else {
            assertEquals(expected.get().getUsername(), user.getUsername());
            assertEquals(expected.get().getPassword(), user.getPassword());
            assertEquals(expected.get().getRole(), user.getRole());
        }

    }

    @Test
    void findNullByUsername() {
        String username = "username";
        String wrongUsername = "totallyWrongUsername";
        User user = new User(1000L, username, "password", Role.USER);

        repository.save(user);

        //when
        Optional<User> expected = repository.findByUsername(wrongUsername);

        if (expected.isPresent()) {
            fail();
        }

        assertEquals(expected, Optional.empty());
    }
}