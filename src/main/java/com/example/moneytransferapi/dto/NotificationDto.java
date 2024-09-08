package com.example.moneytransferapi.dto;

import com.example.moneytransferapi.enums.NotificationType;
import lombok.Data;


@Data
public class NotificationDto {

    private int userId;
    private NotificationType type;
    private Long senderAccountId;
    private Long receiverAccountId;
    private String senderName;
    private String receiverName;
    private float amount;


}
