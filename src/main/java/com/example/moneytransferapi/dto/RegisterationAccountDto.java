package com.example.moneytransferapi.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class RegisterationAccountDto {

    @NotBlank(message = "type cannot be empty")
    private String type;


    @Positive(message = "balance should be positive number")
    private float balance;


}
