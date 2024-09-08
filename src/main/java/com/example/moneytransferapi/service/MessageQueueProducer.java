package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.NotificationDto;

public interface MessageQueueProducer {

    public void sendMessage(NotificationDto notificationDto);
}
