
package com.jobtracker.jobtracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async  // This makes email sending happen in background
    public void sendStatusUpdateEmail(String toEmail,
                                      String userName,
                                      String company,
                                      String role,
                                      String oldStatus,
                                      String newStatus) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Application Status Updated - " + company);
            message.setText(
                    "Hi " + userName + ",\n\n" +
                            "Your application status has been updated!\n\n" +
                            "Company  : " + company + "\n" +
                            "Role     : " + role + "\n" +
                            "Status   : " + oldStatus + " → " + newStatus + "\n\n" +
                            "Best of luck!\n" +
                            "Job Tracker App"
            );

            mailSender.send(message);
            log.info("Email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }
}