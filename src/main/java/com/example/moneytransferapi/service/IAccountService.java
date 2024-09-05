package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface IAccountService {


    ResponseAccountDto createAccount(RegisterationAccountDto registerationDto, HttpServletRequest request);

    ResponseTransactionDTO createTrascation(RequestTrascationDto requestTrascationDto, HttpServletRequest request);

    List<AccountDTO> viewAllUserAccounts(HttpServletRequest request);

    AccountDTO getAccountById(HttpServletRequest request,Long accountId);

    BalanceDto getAccountBalance(HttpServletRequest request,Long accountId);
     Page<TransactionDto> getTransactionForAccount(HttpServletRequest request, ViewTransactionsDto viewTransactionsDto);

}