package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.LoginDto;
import com.example.moneytransferapi.dto.RegistrationDto;
import com.example.moneytransferapi.dto.TokenDto;
import com.example.moneytransferapi.dto.UserDto;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.enums.Role;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.service.UserService;
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

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegistrationDto newUser, BindingResult bindingResult) {
        try {
            User savedUser = userService.createUser(newUser, bindingResult);

            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (InvalidUserDataException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {

        TokenDto token =  userService.login(loginDto,bindingResult);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenDto tokenDto){
        System.out.println("Received TokenDto: " + tokenDto.getAccessToken());
        return  ResponseEntity.ok(userService.logout(tokenDto));
    }


}