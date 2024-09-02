package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.entity.Account;
import com.example.moneytransferapi.entity.Transaction;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.enums.TransactionStatus;
import com.example.moneytransferapi.exception.InvalidAccountData;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.exception.TrancationBalanceException;
import com.example.moneytransferapi.exception.UnauthorizedAccessException;
import com.example.moneytransferapi.repositorie.AccountRepository;
import com.example.moneytransferapi.repositorie.TrascationRepository;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.utilitys.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper mapper;
    private final TrascationRepository trascationRepository;


    @Override
    public ResponseAccountDto createAccount(RegisterationAccountDto registrationDto, BindingResult bindingResult, HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        int currentUserId = jwtUtil.extractUserId(token);
        Optional<User> currentOptional  = userRepository.findById(currentUserId);
        if (currentOptional.isEmpty()){
            throw new InvalidUserDataException("user not found");
        }
        User currentUser = currentOptional.get();

        float roundedBalance = Math.round(registrationDto.getBalance() * 100.0f) / 100.0f;
        long balanceAsLong = (long) (roundedBalance * 100);

        Account account = mapper.map(registrationDto, Account.class);
        account.setUser(currentUser);
        account.setBalance(balanceAsLong);
        Account saved = accountRepository.save(account);

        ResponseAccountDto responseAccountDto = mapper.map(saved,ResponseAccountDto.class);
        responseAccountDto.setBalance(responseAccountDto.getBalance()/100);
        return  responseAccountDto;

    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseTransactionDTO createTrascation(RequestTrascationDto requestTrascationDto, BindingResult bindingResult, HttpServletRequest request) {


        float roundedBalance = Math.round(requestTrascationDto.getAmount() * 100.0f) / 100.0f;
        long amountInLong = (long) (roundedBalance * 100);

        // account
        String token = jwtUtil.getTokenFromRequest(request);
        int currentUserId = jwtUtil.extractUserId(token);
        Optional<Account> currentAccountOpt = accountRepository.findById(requestTrascationDto.getSenderAccountNum());
        Optional<Account> reciverAccountOpt = accountRepository.findById(requestTrascationDto.getReciverAccountNum());
        if (currentAccountOpt.isEmpty() || reciverAccountOpt.isEmpty()){
            throw new InvalidAccountData("Account Not Found");
        }
        Account senderAccount  = currentAccountOpt.get();
        Account reciverAccount = reciverAccountOpt.get();
        if (senderAccount.getUser().getId()!= currentUserId){
            throw  new UnauthorizedAccessException("unauthorized");
        }
        if (senderAccount.getBalance() < amountInLong){
            throw new TrancationBalanceException("Balance not enough");
        }
        senderAccount.setBalance(senderAccount.getBalance()-amountInLong);
        reciverAccount.setBalance(reciverAccount.getBalance()+amountInLong);


        accountRepository.save(senderAccount);
        accountRepository.save(reciverAccount);
        Transaction transaction = Transaction.builder().amoumt(amountInLong)
                .status(TransactionStatus.SUCCEED)
                .senderAccount(senderAccount)
                .receiverAccount(reciverAccount).build();


        trascationRepository.save(transaction);

        return  ResponseTransactionDTO.builder()
                .reciverAccountNum(reciverAccount.getId())
                .senderAccountNum(senderAccount.getId())
                .trascationTime(LocalDateTime.now())
                .amount(amountInLong)
                .status(TransactionStatus.SUCCEED)
                .build();


    }

    @Override
    public List<AccountDTO> viewAllUserAccounts(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        int currentUserId = jwtUtil.extractUserId(token);
        List<Account> accounts = accountRepository.findByUserId(currentUserId);
        return accounts.stream().map(account -> mapper.map(account,AccountDTO.class)).toList();
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        return null;
    }

    @Override
    public BalanceDto getAccountBalance(Long accountId) {
        return null;
    }
}
