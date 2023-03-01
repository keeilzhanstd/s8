package com.s8.keeilzhanstd.challenge.testcontainers.containers;

import org.testcontainers.containers.MySQLContainer;

public class MySQLTestContainer extends MySQLContainer<MySQLTestContainer> {

    public final static String IMAGE = "mysql:8-oracle";
    public final static String DB_NAME = "test";
    public static MySQLContainer container;

    public MySQLTestContainer() {
        super(IMAGE);
    }

    public static MySQLContainer getInstance() {
        if(container == null) {
            container = new MySQLTestContainer().withDatabaseName(DB_NAME);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USER", container.getUsername());
        System.setProperty("DB_PWD", container.getPassword());
    }

    @Override
    public void stop() {}

}
