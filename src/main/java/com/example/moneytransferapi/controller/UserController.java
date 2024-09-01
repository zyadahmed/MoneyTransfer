package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.enums.Role;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.service.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/create")
    public ResponseEntity<ResponseUserDTo> createUser(@Valid @RequestBody RegistrationDto newUser, BindingResult bindingResult) {
            ResponseUserDTo savedUser = userService.createUser(newUser, bindingResult);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<TokensDto> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        TokensDto token =  userService.login(loginDto,bindingResult);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokensDto tokenDto){
        System.out.println("Received TokenDto: " + tokenDto.getAccessToken());
        return  ResponseEntity.ok(userService.logout(tokenDto));
    }

}