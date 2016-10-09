package com.grudus;

import com.grudus.repositories.SubjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExamsHelperWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamsHelperWebApplication.class, args);
	}


	@Bean
	public CommandLineRunner run(SubjectRepository subjectRepository) {
		return args -> {
			System.out.println(subjectRepository.findByUserIdAndAndroidId(24L, 18L));
		};
	}
}
