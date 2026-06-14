package com.jobtracker.jobtracker.messaging;

import com.jobtracker.jobtracker.event.StatusUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public void sendStatusUpdateNotification(StatusUpdateEvent event) {
        log.info("Publishing status update event for: {}", event.getUserEmail());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info("Event published successfully to queue");
    }
}
