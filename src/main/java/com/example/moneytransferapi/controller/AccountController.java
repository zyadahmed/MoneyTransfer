package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.service.AccountServiceImpl;
import com.example.moneytransferapi.service.IAccountService;
import jakarta.servlet.http.HttpServletRequest;
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

    private final IAccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<ResponseAccountDto> createAccount(@RequestBody @Valid RegisterationAccountDto registerationAccountDto,BindingResult bindingResult, HttpServletRequest request){
       ResponseAccountDto response =  accountService.createAccount(registerationAccountDto,bindingResult,request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/transaction")
    public ResponseEntity<ResponseTransactionDTO> createTransaction(
            @RequestBody RequestTrascationDto requestTransactionDto,
            BindingResult bindingResult,
            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        ResponseTransactionDTO response = accountService.createTrascation(requestTransactionDto, bindingResult, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ViewUserAccount")
    public List<AccountDTO> viewAllUserAccounts(HttpServletRequest request) {
        return accountService.viewAllUserAccounts(request);
    }

    @GetMapping("/GetAccountByID")
    public AccountDTO viewAccountByID(@RequestParam Long accountID) {
        return accountService.getAccountById(accountID);
    }

    @GetMapping("/GetAccountBalance")
    public BalanceDto viewAccountBalance(@RequestParam Long accountID) {
        return accountService.getAccountBalance(accountID);
    }
}