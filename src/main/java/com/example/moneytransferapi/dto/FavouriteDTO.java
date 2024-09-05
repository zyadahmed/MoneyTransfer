package com.example.moneytransferapi.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FavouriteDTO {
    private int userId;
    private Long accountId;
    private String name;
}
