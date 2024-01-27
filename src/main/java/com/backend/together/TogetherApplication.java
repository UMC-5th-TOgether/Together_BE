package com.backend.together;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EntityScan(basePackages = {"com.backend.domain"})
@SpringBootApplication
@EnableJpaAuditing
public class TogetherApplication {

	public static void main(String[] args) {
		SpringApplication.run(TogetherApplication.class, args);
	}

}
