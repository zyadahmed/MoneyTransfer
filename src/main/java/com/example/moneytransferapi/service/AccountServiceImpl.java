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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper mapper;
    private final TrascationRepository trascationRepository;


    @Override
    public ResponseAccountDto createAccount(RegisterationAccountDto registrationDto, HttpServletRequest request) {
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
    public ResponseTransactionDTO createTrascation(RequestTrascationDto requestTrascationDto , HttpServletRequest request) {

        float roundedBalance = Math.round(requestTrascationDto.getAmount() * 100.0f) / 100.0f;
        long amountInLong = (long) (roundedBalance * 100);

        String token = jwtUtil.getTokenFromRequest(request);
        int currentUserId = jwtUtil.extractUserId(token);
        Account senderAccount = accountRepository.findById(requestTrascationDto.getSenderAccountNum())
                .orElseThrow(() -> new InvalidAccountData("Sender account not found"));
        Account receiverAccount = accountRepository.findById(requestTrascationDto.getReciverAccountNum())
                .orElseThrow(() -> new InvalidAccountData("Receiver account not found"));

        if (senderAccount.getUser().getId()!= currentUserId){
            throw  new UnauthorizedAccessException("unauthorized");
        }
        if (senderAccount.getBalance() < amountInLong){
            throw new TrancationBalanceException("Balance not enough");
        }
        senderAccount.setBalance(senderAccount.getBalance()-amountInLong);
        receiverAccount.setBalance(receiverAccount.getBalance()+amountInLong);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        Transaction transaction = Transaction.builder().amoumt(amountInLong)
                .status(TransactionStatus.SUCCEED)
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount).build();

        trascationRepository.save(transaction);

        return  ResponseTransactionDTO.builder()
                .reciverAccountNum(receiverAccount.getId())
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

        return accounts.stream()
                .map(account -> {
                    AccountDTO accountDTO = mapper.map(account, AccountDTO.class);
                    accountDTO.setBalance( (account.getBalance() / 100.0f));
                    return accountDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountById(HttpServletRequest request,Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new InvalidUserDataException("Account not found"));

        String token = jwtUtil.getTokenFromRequest(request);
        if (!jwtUtil.extractUserId(token).equals(account.getUser().getId())){
            throw new UnauthorizedAccessException("Unauthorized");
        }

        AccountDTO dto =  mapper.map(account,AccountDTO.class);
        dto.setBalance(dto.getBalance()/100);
        return dto;
    }

    @Override
    public BalanceDto getAccountBalance(HttpServletRequest request,Long accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new InvalidAccountData("Account not found");
        }
        Account account = accountOpt.get();
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setBalance((float) account.getBalance() /100);
        return balanceDto;
    }

}
