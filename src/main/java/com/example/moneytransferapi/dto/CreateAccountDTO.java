package com.example.moneytransferapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateAccountDTO {
    @NotBlank
    private String type;
    @NotBlank
    private Long balance;

    @NotBlank
    private int userId;
}
