package com.example.moneytransferapi.service;


import com.example.moneytransferapi.dto.AccountDTO;
import com.example.moneytransferapi.dto.CreateAccountDTO;
import com.example.moneytransferapi.entity.Account;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.exception.InvalidAccountData;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.exception.UnauthorizedAccessException;
import com.example.moneytransferapi.exception.UserNotFoundException;
import com.example.moneytransferapi.repositorie.AccountRepository;
import com.example.moneytransferapi.repositorie.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public AccountDTO createAccount(CreateAccountDTO createAccountDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Invalid Account Registration Data: ");
            bindingResult.getAllErrors().forEach(error ->
                    errorMessage.append(error.getDefaultMessage()).append("; ")
            );
            throw new InvalidAccountData(errorMessage.toString());
        }

        Optional<User> currentUser = userRepository.findById(createAccountDTO.getUserId());
        if (currentUser.isEmpty()){
            throw new UserNotFoundException("User Not Found");
        }
        User u = currentUser.get();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!u.getEmail().equals(userDetails.getUsername())){
            throw new UnauthorizedAccessException("unauthorized ");
        }
//        Account account = new Account();
//        account.setType(createAccountDTO.getType());
//        account.setBalance(createAccountDTO.getBalance());
//        account.setCreated_at(new Date());
//        account.setUser(u);
        Account account = mapper.map(createAccountDTO,Account.class);
//        mapper.map()

        Account savedAccount = accountRepository.save(account);
        return mapper.map(savedAccount, AccountDTO.class);

    }
    public List<AccountDTO> getUserAccountsByMail() {
        List<Account> accounts = accountRepository.findByUser_Email(getCurrentUserEmail());
        return accounts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public AccountDTO mapToDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setType(account.getType());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setCreated_at(account.getCreated_at());
        accountDTO.setUserId(account.getUser().getId());
        return accountDTO;
    }
    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }



}
