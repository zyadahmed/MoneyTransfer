package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.LoginDto;
import com.example.moneytransferapi.dto.RegistrationDto;
import com.example.moneytransferapi.dto.ResponseUserDTo;
import com.example.moneytransferapi.dto.TokensDto;
import org.springframework.validation.BindingResult;

public interface IUserService {
    ResponseUserDTo createUser(RegistrationDto newUser, BindingResult bindingResult);

    TokensDto login(LoginDto loginDto, BindingResult bindingResult);

    String logout(TokensDto tokenDto);
}
