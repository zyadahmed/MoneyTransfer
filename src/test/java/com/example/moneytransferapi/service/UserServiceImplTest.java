package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.enums.Role;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.utilitys.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ModelMapper mapper;

    @Mock
    private TokenService tokenService;

    @Mock
    private SessionService sessionService;

    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserServiceImpl userService;

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

    void testCreateUser_Success() {
        RegistrationDto registrationDto = new RegistrationDto("ziad", "zyad@gmail.com", "Ziad@123z", "Egypt", new Date(2000, 1, 1));
        User user = new User();
        user.setEmail("zyad@gmail.com");
        user.setPassword("encodedPassword");
        user.setCountry("Egypt");
        user.setName("ziad");
        user.setDateofbirth(new Date(2000, 1, 1));
        user.setRole(Role.USER);

        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("encodedPassword");
        when(mapper.map(registrationDto, User.class)).thenReturn(user);
        when(userRepository.saveAndFlush(user)).thenReturn(user);
        when(mapper.map(user, ResponseUserDTo.class)).thenReturn(new ResponseUserDTo());

        ResponseUserDTo response = userService.createUser(registrationDto);

        assertThat(response).isNotNull();
        verify(userRepository).saveAndFlush(user);
    }

    @Test
    void testCreateUser_existEmail(){
        RegistrationDto registrationDto = new RegistrationDto("ziad", "zyad@gmail.com", "Ziad@123z", "Egypt", new Date(2000, 1, 1));

        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        assertThrows(InvalidUserDataException.class, () -> userService.createUser(registrationDto));

        verify(userRepository, never()).saveAndFlush(any(User.class));
    }
    @Test
    void testLogin_Success() {
        LoginDto loginDto = new LoginDto("zyad@gmail.com", "Ziad@123");
        User user = new User();
        user.setEmail("zyad@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findUserByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockedJwtToken");
        when(tokenService.generateRefreshToken(user)).thenReturn("mockedRefreshToken");

        TokensDto tokensDto = userService.login(loginDto);

        assertThat(tokensDto).isNotNull();
        assertThat(tokensDto.getAccessToken()).isEqualTo("mockedJwtToken");
        assertThat(tokensDto.getRefreshToken()).isEqualTo("mockedRefreshToken");

    }

    @Test
    void testLogin_invalidPassword(){
        LoginDto loginDto = new LoginDto("zyad@gmail.com", "ziad"); // Incorrect password
        User user = new User();
        user.setEmail("zyad@gmail.com");
        user.setPassword("encoded");

        when(userRepository.findUserByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Authentication failed") {});

        assertThrows(InvalidUserDataException.class, () -> userService.login(loginDto));




    }
    @Test
    void testUpdatePassword_Success() {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto("OldPassword", "NewPassword");
        User user = new User();
        user.setPassword("encoded");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(updatePasswordDto.getNewPassword())).thenReturn("encodedNewPassword");

        HttpServletRequest request = mock(HttpServletRequest.class);

        String result = userService.updatePassword(updatePasswordDto, request);

        assertThat(result).isEqualTo("Password updated successfully");
        verify(userRepository).save(user);
    }

    @Test
    void testUpdatePassword_OldPasswordIncorrect() {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto("WrongOldPassword", "NewPassword");
        User user = new User();
        user.setPassword("encodedOldPassword");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())).thenReturn(false);

        HttpServletRequest request = mock(HttpServletRequest.class);

        assertThrows(InvalidUserDataException.class, () -> {
            userService.updatePassword(updatePasswordDto, request);
        });
    }

}