package com.example.videouploader;

import com.example.videouploader.security.JwtAuthenticationFilter;
import com.example.videouploader.serviceImpl.GoogleTokenValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
//@EnableAsync
public class VideouploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideouploaderApplication.class, args);
	}

	@Bean
	public GoogleTokenValidator googleTokenValidator() {
		return new GoogleTokenValidator();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
}
