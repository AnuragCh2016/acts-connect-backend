package com.connect.acts.ActsConnectBackend.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnectionChecker {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionChecker.class);

    private final DataSource dataSource;

    public DatabaseConnectionChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void checkConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                logger.info("Database connection established successfully!");
            } else {
                logger.error("Database connection is not valid.");
            }
        } catch (SQLException e) {
            logger.error("Failed to establish database connection.", e);
        }
    }
}
