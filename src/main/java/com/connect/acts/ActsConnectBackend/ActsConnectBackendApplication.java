package com.connect.acts.ActsConnectBackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActsConnectBackendApplication {

	public static void main(final String[] args) {
		final Dotenv dotenv = Dotenv.load();

		// Set environment variables safely
        ActsConnectBackendApplication.setSystemProperty("PROD_DB_URL", dotenv.get("PROD_DB_URL"));
        ActsConnectBackendApplication.setSystemProperty("PROD_DB_UNAME", dotenv.get("PROD_DB_UNAME"));
        ActsConnectBackendApplication.setSystemProperty("PROD_DB_PWD", dotenv.get("PROD_DB_PWD"));
        ActsConnectBackendApplication.setSystemProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
        ActsConnectBackendApplication.setSystemProperty("JWT_EXPIRATION_TIME", dotenv.get("JWT_EXPIRY"));
        ActsConnectBackendApplication.setSystemProperty("DEV_DB_URL", dotenv.get("DEV_DB_URL"));
        ActsConnectBackendApplication.setSystemProperty("DEV_DB_UNAME", dotenv.get("DEV_DB_UNAME"));
        ActsConnectBackendApplication.setSystemProperty("DEV_DB_PWD", dotenv.get("DEV_DB_PWD"));

		SpringApplication.run(ActsConnectBackendApplication.class, args);
	}

	private static void setSystemProperty(final String key, final String value) {
		if (null != value) {
			System.setProperty(key, value);
		} else {
			System.err.println("Warning: System property " + key + " is not set. Value is null.");
		}
	}
}
