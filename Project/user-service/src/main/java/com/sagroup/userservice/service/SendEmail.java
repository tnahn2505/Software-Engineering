package com.sagroup.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmail {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendResetToken(String email, String token) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(email);
            mailMessage.setSubject("Reset your password");

            String resetUrl = "http://localhost:3000/reset-password?token=" + token;
            mailMessage.setText("Click the link below to reset your password:\n" + resetUrl);

            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("❌ Error sending reset email: " + e.getMessage());
        }
    }

    public void sendAccountCreatedEmail(String toEmail, String rawPassword, String role) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("Your account for NPK School");

            String loginUrl = "http://localhost:3000/login";
            String text = String.format(
                    "🎉 Welcome to NPK School!\n\nYour account has been created.\n\nUsername: %s\nPassword: %s\nRole: %s\n\nYou can log in at: %s\n\nPlease change your password after first login for security.",
                    toEmail, rawPassword, role, loginUrl
            );

            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("❌ Error sending account creation email: " + e.getMessage());
        }
    }
}
