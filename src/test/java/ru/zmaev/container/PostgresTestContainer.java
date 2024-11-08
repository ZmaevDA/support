package ru.zmaev.container;

import org.flywaydb.core.Flyway;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class PostgresTestContainer {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("integration-tests-db").withUsername("username").withPassword("password");

    static {
        postgreSQLContainer.start();
        Flyway flyway = Flyway.configure()
                .dataSource(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
}