package com.example.moneytransferapi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegistrationDto {

    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "not valid Email")
    private String email;
    @NotBlank(message = "password is mandatory")
    @Size(min = 6)
    private String password;

    @NotBlank(message = "Country is required")
    private String country;
    @NotNull(message = "date of birth is required")
    private Date dateofbirth;



}
