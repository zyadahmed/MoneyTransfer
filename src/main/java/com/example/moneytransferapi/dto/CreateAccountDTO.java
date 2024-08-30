package com.example.moneytransferapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateAccountDTO {
    private String type;
    private Long balance;
    private int userId;
}
