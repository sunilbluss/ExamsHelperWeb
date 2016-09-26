package com.grudus.configuration;

import com.grudus.configuration.authentication.UserAuthenticationProvider;
import com.grudus.helpers.EmailSender;
import com.grudus.helpers.SessionIdentifierGenerator;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansMother {

    @Bean
    public MailProperties mailProperties() {
        return new MailProperties();
    }

    @Bean
    public EmailSender emailSender() {
        return new EmailSender(mailProperties());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserAuthenticationProvider userAuthenticationProvider(UserRepository repository) {
        return new UserAuthenticationProvider(repository, passwordEncoder());
    }

}
