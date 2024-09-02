package com.example.moneytransferapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestTrascationDto {

    Long senderAccountNum;
    Long reciverAccountNum;

    float amount;


}
