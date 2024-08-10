package com.connect.acts.ActsConnectBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.connect.acts.ActsConnectBackend")
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        // Set your MySQL-specific connection properties
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/your_database_name");
        dataSource.setUsername("your_username");
        dataSource.setPassword("your_password");

        return dataSource;
    }

    // Additional bean configurations can be added here
}
