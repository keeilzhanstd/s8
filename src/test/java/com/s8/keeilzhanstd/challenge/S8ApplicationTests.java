package com.s8.keeilzhanstd.challenge;

import com.s8.keeilzhanstd.challenge.testcontainers.config.ContainersEnvironment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = S8Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class S8ApplicationTests extends ContainersEnvironment {
    @Test
    void contextLoads() {
    }
}
