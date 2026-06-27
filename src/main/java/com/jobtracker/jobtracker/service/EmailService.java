

package com.jobtracker.jobtracker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void sendStatusUpdateEmail(String toEmail,
                                      String userName,
                                      String company,
                                      String role,
                                      String oldStatus,
                                      String newStatus) {
        try {
            String url = "https://api.brevo.com/v3/smtp/email";

            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json");
            headers.set("api-key", brevoApiKey);
            headers.set("content-type", "application/json");

            Map<String, Object> sender = new HashMap<>();
            sender.put("name", "Job Tracker App");
            sender.put("email", senderEmail);

            Map<String, Object> recipient = new HashMap<>();
            recipient.put("email", toEmail);
            recipient.put("name", userName);

            Map<String, Object> body = new HashMap<>();
            body.put("sender", sender);
            body.put("to", new Object[]{recipient});
            body.put("subject", "Application Status Updated - " + company);
            body.put("htmlContent",
                    "<p>Hi " + userName + ",</p>" +
                            "<p>Your application status has been updated!</p>" +
                            "<p><b>Company:</b> " + company + "<br>" +
                            "<b>Role:</b> " + role + "<br>" +
                            "<b>Status:</b> " + oldStatus + " &rarr; " + newStatus + "</p>" +
                            "<p>Best of luck!<br>Job Tracker App</p>");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                log.info("Email sent successfully to: {}", toEmail);
            } else {
                log.error("Unexpected response sending email: {}", response.getBody());
            }

        } catch (Exception e) {
            log.error("Failed to send email via Brevo API: {}", e.getMessage());
        }
    }
}