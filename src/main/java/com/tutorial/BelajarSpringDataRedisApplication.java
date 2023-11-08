package com.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableRedisRepositories // aktifkan fitur repository redis pada spring data
@EnableCaching // aktifkan spring caching (menyimpan data di memory secara sementara (Caching))
@SpringBootApplication
public class BelajarSpringDataRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(BelajarSpringDataRedisApplication.class, args);
	}

}
