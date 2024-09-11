package com.example.moneytransferapi.dto;

import com.example.moneytransferapi.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class ResponseTransactionDTO {
    Long senderAccountNum;
    Long reciverAccountNum;

    float amount;
    TransactionStatus status;
    LocalDateTime trascationTime;
     String senderName;
     String receiverName;

}
