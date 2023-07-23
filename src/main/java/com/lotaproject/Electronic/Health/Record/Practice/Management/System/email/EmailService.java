package com.lotaproject.Electronic.Health.Record.Practice.Management.System.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final JavaMailSender javaMailSender;

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    @Override
    @Async
    public void send(String to, String email) {

        try{

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("Hello@lota.com");
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
