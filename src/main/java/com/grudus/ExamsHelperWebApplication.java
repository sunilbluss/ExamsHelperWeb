package com.grudus;

import com.grudus.configuration.MailProperties;
import com.grudus.entities.User;
import com.grudus.helpers.EmailSender;
import com.grudus.helpers.JsonHelper;
import com.grudus.helpers.SessionIdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.stream.IntStream;

@SpringBootApplication
public class ExamsHelperWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamsHelperWebApplication.class, args);
	}


	@Bean
	public CommandLineRunner run() {
		return args -> {
			System.err.println(JsonHelper.userToJsonString(new User("kuba", "hehe", "email", Calendar.getInstance().getTime())));
		};
	}
}
