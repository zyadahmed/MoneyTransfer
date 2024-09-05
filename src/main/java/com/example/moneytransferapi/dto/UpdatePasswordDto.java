package com.example.moneytransferapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdatePasswordDto
{

    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String newPassword;
}
