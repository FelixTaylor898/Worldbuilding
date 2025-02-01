package com.java.backend;

import com.java.backend.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@Import(SecurityConfig.class) // Import SecurityConfig explicitly
public class WorldbuildingProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorldbuildingProjectApplication.class, args);
	}

}
