package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.AccountDTO;
import com.example.moneytransferapi.dto.CreateAccountDTO;
import com.example.moneytransferapi.entity.Account;
import com.example.moneytransferapi.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody @Valid CreateAccountDTO createAccountDTO, BindingResult bindingResult) {
        AccountDTO account = accountService.createAccount(createAccountDTO,bindingResult);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<AccountDTO>> getUserAccountsByMail() {
        List<AccountDTO> accounts = accountService.getUserAccountsByMail();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
}