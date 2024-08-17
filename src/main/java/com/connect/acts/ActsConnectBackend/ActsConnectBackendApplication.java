package com.connect.acts.ActsConnectBackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActsConnectBackendApplication {

	public static void main(final String[] args) {
		final Dotenv dotenv = Dotenv.load();
		// set environment variables for prod
		System.setProperty("PROD_DB_URL", dotenv.get("PROD_DB_URL"));
		System.setProperty("PROD_DB_UNAME", dotenv.get("PROD_DB_UNAME"));
		System.setProperty("PROD_DB_PWD", dotenv.get("PROD_DB_PWD"));

		// set jwt env variables
		System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
		System.setProperty("JWT_EXPIRATION_TIME", dotenv.get("JWT_EXPIRY"));

		// set environment variables for dev
		System.setProperty("DEV_DB_URL", dotenv.get("DEV_DB_URL"));
		System.setProperty("DEV_DB_UNAME", dotenv.get("DEV_DB_UNAME"));
		System.setProperty("DEV_DB_PWD", dotenv.get("DEV_DB_PWD"));

		// set CORS origin
		System.setProperty("CORS_ORIGINS", dotenv.get("CORS_ORIGINS"));

		SpringApplication.run(ActsConnectBackendApplication.class, args);
	}
}
