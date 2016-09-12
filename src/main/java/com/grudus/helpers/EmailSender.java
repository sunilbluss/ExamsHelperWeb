package com.grudus.helpers;


import com.grudus.configuration.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

@Component
public class EmailSender {

    public static final String DEFAULT_MESSAGE = "Hello, %s! To complete the registration click the following link: http://%s:%d/add/%s/%s";
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
        mimeMessage.setFrom(new InternetAddress(mailProperties.getUserName()));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipient));
        mimeMessage.setSubject("MESSAGE FROM JAVA");
        mimeMessage.setText(message);

        Transport.send(mimeMessage);
        System.out.println("Sent message successfully...");
    }

    // TODO: 13.09.16 change this request
    public String sendKeyMessageAndGetKey(String userName, String emailRecipient, HttpServletRequest request) throws MessagingException, UnknownHostException {
        String key = generator.nextSessionId();
        send(String.format(DEFAULT_MESSAGE,
                userName,
                request.getLocalAddr(),
                request.getLocalPort(),
                userName,
                key), emailRecipient);
        return key;
    }



    private class SMTPAuthenticator extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = mailProperties.getUserName();
            String password = mailProperties.getPassword();
            return new PasswordAuthentication(username, password);
        }
    }


}