package com.example.moneytransferapi.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDTO {
    private Long id;
    private String type;
    private Long balance;
    private Date created_at;
    private int userId;
}
