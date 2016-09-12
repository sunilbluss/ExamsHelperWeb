package com.grudus.configuration;

import com.grudus.helpers.EmailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
