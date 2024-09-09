package com.example.moneytransferapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto
{

    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String newPassword;
}
