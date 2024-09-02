package com.example.moneytransferapi.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class RegisterationAccountDto {

    @NotBlank(message = "type cannot be empty")
    private String type;

    private float balance;


}
