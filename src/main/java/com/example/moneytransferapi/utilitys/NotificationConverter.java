package com.example.moneytransferapi.utilitys;

import com.example.moneytransferapi.dto.NotificationDto;
import com.example.moneytransferapi.entity.Transaction;
import com.example.moneytransferapi.enums.NotificationType;
import org.springframework.stereotype.Component;


@Component
public class NotificationConverter {
    public NotificationDto createSenderNotification(Transaction transaction) {
        NotificationDto senderNotification = new NotificationDto();
        senderNotification.setUserId(transaction.getSenderAccount().getUser().getId());
        senderNotification.setType(NotificationType.SENDER);
        return getNotificationDto(transaction, senderNotification);
    }

    public NotificationDto createReceiverNotification(Transaction transaction) {
        NotificationDto receiverNotification = new NotificationDto();
        receiverNotification.setUserId(transaction.getReceiverAccount().getUser().getId());
        receiverNotification.setType(NotificationType.RECEIVER);
        return getNotificationDto(transaction, receiverNotification);
    }

    private NotificationDto getNotificationDto(Transaction transaction, NotificationDto receiverNotification) {
        receiverNotification.setSenderAccountId(transaction.getSenderAccount().getId());
        receiverNotification.setReceiverAccountId(transaction.getReceiverAccount().getId());
        receiverNotification.setSenderName(transaction.getSenderAccount().getUser().getName());
        receiverNotification.setReceiverName(transaction.getReceiverAccount().getUser().getName());
        receiverNotification.setAmount(transaction.getAmount().floatValue() / 100);
        return receiverNotification;
    }
}
