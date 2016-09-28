package com.grudus.helpers;


import com.grudus.configuration.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailSender {

    private Properties properties;
    private Session session;

    private final MailProperties mailProperties;

    private final SessionIdentifierGenerator generator;

    private MimeMessage mimeMessage;

    @Autowired
    public EmailSender(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
        properties = new Properties();
        properties.put("mail.smtp.host", mailProperties.getHostName());
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", mailProperties.getPort());
        session = Session.getDefaultInstance(properties, new SMTPAuthenticator());

        generator = new SessionIdentifierGenerator();
    }


    public void send(final String message, final String emailRecipient) throws MessagingException {
        mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(mailProperties.getUsername()));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipient));
        mimeMessage.setSubject(mailProperties.getMessageSubject());
        mimeMessage.setText(message);

        Transport.send(mimeMessage);
        System.out.println("Sent message successfully...");
    }

    public String sendKeyMessageAndGetKey(String username, String emailRecipient) throws MessagingException {
        String key = generator.nextSessionId();
        String message = new MessageBuilder()
                .setKey(key)
                .setUserName(username)
                .build();

        send(message, emailRecipient);

        return key;
    }



    private class SMTPAuthenticator extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = mailProperties.getUsername();
            String password = mailProperties.getPassword();
            return new PasswordAuthentication(username, password);
        }
    }


    private class MessageBuilder {
        private String username;
        private String key;

        public MessageBuilder setUserName(String username) {
            this.username = username;
            return this;
        }

        private MessageBuilder setKey(String key) {
            this.key = key;
            return this;
        }

        private String build() {
            if (username == null || key == null)
                throw new NullPointerException("Username and key cannot be null");

            return String.format(mailProperties.getMessage(),
                    username,
                    username,
                    key);
        }
    }


}