package com.example.moneytransferapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@NoArgsConstructor
@Data
public class ResponseAccountDto {

    private int id;

    private String type;

    private float balance;

    private Date created_at;


}
