package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface IAccountService {


    ResponseAccountDto createAccount(RegisterationAccountDto registerationDto, BindingResult bindingResult, HttpServletRequest request);

    ResponseTransactionDTO createTrascation(RequestTrascationDto requestTrascationDto, BindingResult bindingResult, HttpServletRequest request);

    List<AccountDTO> viewAllUserAccounts(HttpServletRequest request);

    AccountDTO getAccountById(Long accountId);

    BalanceDto getAccountBalance(Long accountId);
}
