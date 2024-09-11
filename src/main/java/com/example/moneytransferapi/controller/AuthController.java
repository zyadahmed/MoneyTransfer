package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.service.IUserService;
import com.example.moneytransferapi.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TokenService tokenService;
    private final IUserService userService;

    @PostMapping("/refresh")
    public ResponseEntity<TokensDto> refreshAccessToken(@RequestBody RefreshTokenDto refreshToken) {
        TokensDto newTokens = tokenService.refreshAccessToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok(newTokens);
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseUserDTo> createUser(@Valid @RequestBody RegistrationDto newUser) {
        ResponseUserDTo savedUser = userService.createUser(newUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<TokensDto> login(@Valid @RequestBody LoginDto loginDto) {
        TokensDto token =  userService.login(loginDto);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokensDto tokenDto){
        System.out.println("Received TokenDto: " + tokenDto.getAccessToken());
        return  ResponseEntity.ok(userService.logout(tokenDto));
    }



}
