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
    }


    public void send(final String message, final String emailRecipient) throws MessagingException {
        mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(mailProperties.getUserName()));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipient));
        mimeMessage.setSubject("MESSAGE FROM JAVA");
        mimeMessage.setText(message);

        Transport.send(mimeMessage);
        System.out.println("Sent message successfully...");
    }



//  ###########################################################################

    private class SMTPAuthenticator extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = mailProperties.getUserName();
            String password = mailProperties.getPassword();
            return new PasswordAuthentication(username, password);
        }
    }



}