package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

public interface IUserService {
    ResponseUserDTo createUser(RegistrationDto newUser);

    TokensDto login(LoginDto loginDto );

    String logout(TokensDto tokenDto);
    String updatePassword(UpdatePasswordDto updatePasswordDto, HttpServletRequest request);
    User getUserWithAccounts(HttpServletRequest request);

}
