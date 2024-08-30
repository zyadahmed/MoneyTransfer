package com.example.moneytransferapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;


@Getter
public class LoginDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
