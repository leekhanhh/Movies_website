package com.ecommerce.website.movie.service;

import com.ecommerce.website.movie.config.AsyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@Slf4j
public class AsyncService {
    @Autowired
    private EmailService emailService;
    @Autowired
    AsyncConfig asyncConfig;

    @Async("threadPoolExecutor")
    public void sendEmail(String email, String subject, String message) {
        Runnable task = () -> {
            log.info("Sending email to " + email);
            try {
                emailService.sendEmail(email, subject, message);
            } catch (MessagingException e) {
                log.error("Failed to send email to " + email, e);
            }
        };
        asyncConfig.threadPoolTaskExecutorForAsyncTask().execute(task);
    }
}
