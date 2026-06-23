package com.dmytro.language_learning_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class LanguageLearningApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LanguageLearningApiApplication.class, args);
	}

}
