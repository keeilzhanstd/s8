package com.s8.keeilzhanstd.challenge.testcontainers.config;

import com.s8.keeilzhanstd.challenge.testcontainers.containers.MySQLTestContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {

    @Container
    public static MySQLContainer mySQLContainer = MySQLTestContainer.getInstance();

}
