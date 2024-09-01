package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.RefreshTokenDto;
import com.example.moneytransferapi.dto.TokensDto;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<TokensDto> refreshAccessToken(@RequestBody RefreshTokenDto refreshToken) {
        TokensDto newTokens = tokenService.refreshAccessToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok(newTokens);
    }



}
