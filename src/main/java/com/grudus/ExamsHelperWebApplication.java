package com.grudus;

import com.grudus.entities.Role;
import com.grudus.entities.User;
import com.grudus.helpers.JsonHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Calendar;

@SpringBootApplication
public class ExamsHelperWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamsHelperWebApplication.class, args);
	}


	@Bean
	public CommandLineRunner run() {
		return args -> {
			System.err.println(JsonHelper.userToJsonString(new User("kuba", "hehe", "email", Calendar.getInstance().getTime(), Role.ROLE_USER)));
		};
	}
}
