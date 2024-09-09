package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.NotificationDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class RabbitMQProducer implements MessageQueueProducer{

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(NotificationDto notificationDto) {
        rabbitTemplate.convertAndSend("notification_exchange","notification_routing",notificationDto);

    }
}
