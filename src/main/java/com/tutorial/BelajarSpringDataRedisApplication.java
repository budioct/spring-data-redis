package com.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BelajarSpringDataRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(BelajarSpringDataRedisApplication.class, args);
	}

}
