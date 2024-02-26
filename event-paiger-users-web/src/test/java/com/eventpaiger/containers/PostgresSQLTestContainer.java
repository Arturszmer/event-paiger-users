package com.eventpaiger.containers;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresSQLTestContainer {
    public static JdbcDatabaseContainer<?> createServer() {
        return new PostgreSQLContainer("postgres:11.1")
                .withDatabaseName("event-paiger-users")
                .withUsername("ep-user")
                .withPassword("ep-secret");
    }
}
