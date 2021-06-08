package com.kon.EShop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

@Service
public class SendHTMLEmail {

    @Value("${spring.mail.username}")
    private String userName;

    private final Message message;

    public SendHTMLEmail(Message message) {
        this.message = message;
    }

    public void send(String emailTo, String subject, String content) {
        try {
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(userName));
            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            // Set Subject: header field
            message.setSubject(subject);
            // Send the actual HTML message, as big as you like
            message.setContent(content, "text/html; charset=UTF-8");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
