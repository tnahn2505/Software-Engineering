package com.sagroup.userservice.service;

// Java program to send email

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;


@Service
public class SendEmail {
    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender javaMailSender;
    public void sendMail(String username)
    {
        try {
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(username);
            mailMessage.setText("Account created");
            mailMessage.setSubject("Teacher account created");

            // Sending the mail
            javaMailSender.send(mailMessage);
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

