package com.example.moneytransferapi.dto;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto {

        private int id;

        private float amount;

        private LocalDateTime createdTimeStamp;
        private Long senderAccountId;
        private Long reciverAccountId;
        String senderName;
        String receiverName;
}
