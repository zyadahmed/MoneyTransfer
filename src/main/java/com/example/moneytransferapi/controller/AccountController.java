package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.service.AccountServiceImpl;
import com.example.moneytransferapi.service.IAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
    public ResponseEntity<ResponseAccountDto> createAccount(@RequestBody @Valid RegisterationAccountDto registerationAccountDto , HttpServletRequest request){
       ResponseAccountDto response =  accountService.createAccount(registerationAccountDto,request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/transaction")
    public ResponseEntity<ResponseTransactionDTO> createTransaction(
            @RequestBody RequestTrascationDto requestTransactionDto,
            HttpServletRequest request) {

        ResponseTransactionDTO response = accountService.createTrascation(requestTransactionDto, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ViewUserAccount")
    public List<AccountDTO> viewAllUserAccounts(HttpServletRequest request) {
        return accountService.viewAllUserAccounts(request);
    }

    @GetMapping("/GetAccountByID/{accountID}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable  Long accountID, HttpServletRequest request) {
        return new ResponseEntity<>(accountService.getAccountById(request,accountID), HttpStatus.OK);
    }

    @GetMapping("/GetAccountBalance/{accountID}")
    public ResponseEntity<BalanceDto> getAccountBalance(@PathVariable Long accountID,HttpServletRequest request) {
        return new ResponseEntity<>(accountService.getAccountBalance(request,accountID), HttpStatus.OK);
    }
}