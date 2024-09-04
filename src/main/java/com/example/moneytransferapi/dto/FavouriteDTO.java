package com.example.moneytransferapi.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavouriteDTO {
    private int userId;
    private int accountId;
    private String name;
}
