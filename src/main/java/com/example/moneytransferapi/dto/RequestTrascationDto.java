package com.example.moneytransferapi.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestTrascationDto {


    @Size(min = 8, max = 8, message = "The Sender Account Num not valid.")
    Long senderAccountNum;
    @Size(min = 8, max = 8, message = "The receiver Account Num not valid.")
    Long reciverAccountNum;


    @Positive(message = "Transaction must be  positive value")
    float amount;

     String senderName;
     String receiverName;



}
