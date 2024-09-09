package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.enums.Role;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final IUserService userService;

    @PutMapping("/update-password")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto, HttpServletRequest request){
        return ResponseEntity.ok(userService.updatePassword(updatePasswordDto,request));
    }
    @GetMapping("/info")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<User> getUserInfo(HttpServletRequest request){
        return ResponseEntity.ok(userService.getUserWithAccounts(request));
    }





}