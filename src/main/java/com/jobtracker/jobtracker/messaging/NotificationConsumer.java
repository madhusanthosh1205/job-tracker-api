package com.jobtracker.jobtracker.messaging;


import com.jobtracker.jobtracker.event.StatusUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void handleStatusUpdate(StatusUpdateEvent event) {
        log.info("Received status update event for: {}", event.getUserEmail());
        sendEmail(event);
    }

    private void sendEmail(StatusUpdateEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getUserEmail());
            message.setSubject("Application Status Updated - " + event.getCompany());
            message.setText(
                    "Hi " + event.getUserName() + ",\n\n" +
                            "Your application status has been updated!\n\n" +
                            "Company  : " + event.getCompany() + "\n" +
                            "Role     : " + event.getRole() + "\n" +
                            "Status   : " + event.getOldStatus() + " → " + event.getNewStatus() + "\n\n" +
                            "Best of luck!\n" +
                            "Job Tracker App"
            );

            mailSender.send(message);
            log.info("Email sent successfully to: {}", event.getUserEmail());

        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }
}
