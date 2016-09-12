package com.grudus;

import com.grudus.configuration.MailProperties;
import com.grudus.helpers.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.mail.MessagingException;

@SpringBootApplication
public class ExamsHelperWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamsHelperWebApplication.class, args);
	}


	@Bean
	public CommandLineRunner run(EmailSender emailSender) {
		return args -> {
		};
	}
}
