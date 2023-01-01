package com.example.smartorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SmartorderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartorderApplication.class, args);
	}

}
