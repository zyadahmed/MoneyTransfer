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
import com.example.moneytransferapi.utilitys.NotificationConverter;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {


    @Mock
    private NotificationConverter notificationConverter;

    @Mock
    private RabbitMQProducer rabbitMQProducer;
    @Mock
    private  AccountRepository accountRepository;

    @Mock

    private  UserRepository userRepository;
    @Mock

    private  JwtUtil jwtUtil;
    @Mock


    private  ModelMapper mapper;
    @Mock
    private  TrascationRepository trascationRepository;


    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setup() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        when(jwtUtil.getTokenFromRequest(any(HttpServletRequest.class))).thenReturn("mockedToken");
    }

    @Test
    void createAccount_validData() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "mockedToken";
        int userId = 123;

        RegisterationAccountDto registrationDto = new RegisterationAccountDto();
        registrationDto.setType("saving");
        registrationDto.setBalance(100.0f);

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName("ziad");

        Account mockAccount = new Account();
        mockAccount.setUser(mockUser);
        mockAccount.setBalance(10000L);
        mockAccount.setCreated_at(Date.from(Instant.now()));

        ResponseAccountDto expectedResponse = new ResponseAccountDto();
        expectedResponse.setBalance(100.0f);

        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mapper.map(any(RegisterationAccountDto.class), eq(Account.class))).thenReturn(mockAccount);
        when(accountRepository.saveAndFlush(any(Account.class))).thenReturn(mockAccount);
        when(mapper.map(any(Account.class), eq(ResponseAccountDto.class))).thenReturn(expectedResponse);

        ResponseAccountDto actualResponse = accountService.createAccount(registrationDto, request);

        assertEquals(expectedResponse.getBalance(), actualResponse.getBalance());
        verify(accountRepository).saveAndFlush(any(Account.class));

    }
    @Test
    void createAccount_userNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "mockedToken";
        int userId = 123;

        RegisterationAccountDto registrationDto = new RegisterationAccountDto();
        registrationDto.setBalance(100.0f);

        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(InvalidUserDataException.class, () -> {
            accountService.createAccount(registrationDto, request);
        });

        verify(userRepository).findById(userId);
    }
    @Test
    void createTrascation_validData1() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "mockedToken";
        long amountInLong = 5000L;
        int userId = 2;

        RequestTrascationDto requestDto = new RequestTrascationDto();
        requestDto.setAmount(50.0f);
        requestDto.setSenderAccountNum(1L);
        requestDto.setReciverAccountNum(2L);

        Account senderAccount = new Account();
        senderAccount.setId(1L);
        User user = new User();
        user.setId(2);
        senderAccount.setUser(user);
        senderAccount.setBalance(10000L);

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setBalance(2000L);

        Transaction transaction = Transaction.builder()
                .amount(amountInLong)
                .status(TransactionStatus.SUCCEED)
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .build();

        when(jwtUtil.getTokenFromRequest(any(HttpServletRequest.class))).thenReturn(token);
        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiverAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(senderAccount);
        when(trascationRepository.save(any(Transaction.class))).thenReturn(transaction);

        ResponseTransactionDTO response = accountService.createTrascation(requestDto, request);

        verify(trascationRepository).save(any(Transaction.class));
        assertNotNull(response);

    }




    @Test
    void createTrascation_senderAccountNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "mockedToken";
        int userId = 12;

        RequestTrascationDto requestDto = new RequestTrascationDto();
        requestDto.setAmount(50.0f);
        requestDto.setSenderAccountNum(1L);
        requestDto.setReciverAccountNum(2L);

        when(jwtUtil.getTokenFromRequest(any(HttpServletRequest.class))).thenReturn(token);
        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidAccountData.class, () -> {
            accountService.createTrascation(requestDto, request);
        });

        verify(accountRepository, never()).save(any(Account.class));
        verify(trascationRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createTrascation_balanceNotEnough() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "mockedToken";
        int userId = 123;
        long amount = 5000L;

        RequestTrascationDto requestDto = new RequestTrascationDto();
        requestDto.setAmount(50.0f);
        requestDto.setSenderAccountNum(1L);
        requestDto.setReciverAccountNum(2L);

        Account senderAccount = new Account();
        senderAccount.setId(1L);
        User user = new User();
        user.setId(123);
        senderAccount.setUser(user);
        senderAccount.setBalance(4000L);

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setBalance(2000L);

        when(jwtUtil.getTokenFromRequest(any(HttpServletRequest.class))).thenReturn(token);
        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiverAccount));

        assertThrows(TrancationBalanceException.class, () -> {
            accountService.createTrascation(requestDto, request);
        });

        verify(accountRepository, never()).save(any(Account.class));
        verify(trascationRepository, never()).save(any(Transaction.class));
    }
    @Test
    void createTrascation_unauthorizedAccess() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "mockedToken";
        int userId = 11;

        RequestTrascationDto requestDto = new RequestTrascationDto();
        requestDto.setAmount(50.0f);
        requestDto.setSenderAccountNum(1L);
        requestDto.setReciverAccountNum(2L);

        Account senderAccount = new Account();
        senderAccount.setId(1L);
        User user = new User();
        user.setId(2002);
        senderAccount.setUser(user);
        senderAccount.setBalance(10000L);

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setBalance(2000L);

        when(jwtUtil.getTokenFromRequest(any(HttpServletRequest.class))).thenReturn(token);
        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiverAccount));

        assertThrows(UnauthorizedAccessException.class, () -> {
            accountService.createTrascation(requestDto, request);
        });

        verify(accountRepository, never()).save(any(Account.class));
        verify(trascationRepository, never()).save(any(Transaction.class));
    }

    @Test
    void viewAllUserAccounts_succees(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "mockedToken";
        int userId = 1;
        Account account = new Account();
        account.setBalance(100L);
        account.setType("saving");
        when(jwtUtil.getTokenFromRequest(any(HttpServletRequest.class))).thenReturn(token);
        when(jwtUtil.extractUserId(anyString())).thenReturn(userId);
        when(accountRepository.findByUserId(userId)).thenReturn(List.of(account));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(1.0f);
        accountDTO.setType("saving");
        assertThat(accountDTO.getBalance()).isEqualTo((float) account.getBalance() /100);
        assertThat(accountDTO.getType()).isEqualTo("saving");


    }
    @Test
    void viewAllUserAccounts_userNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "mockedToken";
        int userId = 1;

        when(jwtUtil.getTokenFromRequest(any(HttpServletRequest.class))).thenReturn(token);
        when(jwtUtil.extractUserId(anyString())).thenReturn(userId);
        when(accountRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<AccountDTO> accountDTOs = accountService.viewAllUserAccounts(request);

        assertThat(accountDTOs.size()).isEqualTo(0);

    }

    @Test
    void getAccountById_success() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long accountId = 1L;
        String token = "mockedToken";
        int userId = 1;

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(10000L);

        User user = new User();
        user.setId(userId);
        account.setUser(user);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(jwtUtil.getTokenFromRequest(request)).thenReturn(token);
        when(jwtUtil.extractUserId(token)).thenReturn(userId);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(10000);
        accountDTO.setType("saving");

        when(mapper.map(account, AccountDTO.class)).thenReturn(accountDTO);

        AccountDTO result = accountService.getAccountById(request, accountId);

        assertThat(result.getBalance()).isEqualTo(100.0f);
        assertThat(result.getType()).isEqualTo("saving");
    }
    @Test
    void getAccountById_accountNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(InvalidUserDataException.class, () -> {
            accountService.getAccountById(request, accountId);
        });
    }

    @Test
    void getAccountById_unAuthorizedUser(){
        int userId= 5 ;
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long accountId = 1L;
        User user = new User();
        user.setId(7);
        Account account = new Account();
        account.setUser(user);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        assertThrows(UnauthorizedAccessException.class,()->accountService.getAccountById(request,accountId));

    }

    @Test
    void getAccountBalance_success() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(100L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        BalanceDto result = accountService.getAccountBalance(request, 1L);
        assertThat(result.getBalance()).isEqualTo(1.0f);

    }
    @Test
    void getAccountBalance_accountNotCorrect() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Account account = new Account();
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InvalidAccountData.class,()->accountService.getAccountBalance(request,1L));

    }



}



