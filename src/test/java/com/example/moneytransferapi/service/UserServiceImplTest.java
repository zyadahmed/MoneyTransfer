package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.enums.Role;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.repositorie.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private ModelMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
    void testCreateUser_existEmail(){
        RegistrationDto registrationDto = new RegistrationDto("ziad", "zyad@gmail.com", "Ziad@123z", "Egypt", new Date(2000, 1, 1));

        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        assertThrows(InvalidUserDataException.class, () -> userService.createUser(registrationDto));

        verify(userRepository, never()).saveAndFlush(any(User.class));
    }
    @Test
    void testLogin_Success(){
        LoginDto loginDto = new LoginDto("zyad@gmail.com","Ziad@123");
        User user = new User();
        user.setEmail("zyad@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findUserByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(),user.getPassword())).thenReturn(true);

        TokensDto tokensDto = userService.login(loginDto);
        assertThat(tokensDto).isNotNull();

    }

    @Test
    void testLogin_invalidPassword(){
        LoginDto loginDto = new LoginDto("zyad@gmail.com", "ziad");
        User user = new User();
        user.setEmail("zyad@gmail.com");
        user.setPassword("encoded");
        when(userRepository.findUserByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidUserDataException.class,()->userService.login(loginDto));


    }
    @Test
    void testUpdatePassword_Success() {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto("OldPassword", "NewPassword");
        User user = new User();
        user.setPassword("encoded");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(updatePasswordDto.getNewPassword())).thenReturn("encodedNewPassword");

        String result = userService.updatePassword(updatePasswordDto, mock(HttpServletRequest.class));

        assertThat(result).isEqualTo("Password updated successfully");
        verify(userRepository).save(user);
    }

    @Test
    void testUpdatePassword_OldPasswordIncorrect() {
        // Arrange
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto("WrongOldPassword", "NewPassword");
        User user = new User();
        user.setPassword("encodedOldPassword");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidUserDataException.class, () -> userService.updatePassword(updatePasswordDto, mock(HttpServletRequest.class)));
    }

}