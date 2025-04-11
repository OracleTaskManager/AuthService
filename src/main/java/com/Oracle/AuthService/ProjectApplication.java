package com.Oracle.AuthService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.Oracle.AuthService")
public class ProjectApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing() // Optional: avoids crash if .env is missing
				.load();

		System.setProperty("JWT_SECRET_ORACLE", dotenv.get("JWT_SECRET_ORACLE"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("TELEGRAM_BOT_SECRET", dotenv.get("TELEGRAM_BOT_SECRET"));

		SpringApplication.run(ProjectApplication.class, args);
	}
}
