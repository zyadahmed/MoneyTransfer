package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.LoginDto;
import com.example.moneytransferapi.dto.RegistrationDto;
import com.example.moneytransferapi.dto.TokenDto;
import com.example.moneytransferapi.dto.UserDto;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.enums.Role;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.securityconfig.JwtAuthFIlter;
import com.example.moneytransferapi.utilitys.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserService {


    private RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final TokenService tokenService;
    private static final String TOKEN_PREFIX = "b:";



    public User createUser(RegistrationDto newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Invalid user data: ");
            bindingResult.getAllErrors().forEach(error ->
                    errorMessage.append(error.getDefaultMessage()).append("; ")
            );
            throw new InvalidUserDataException(errorMessage.toString());
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new InvalidUserDataException("Email already exists.");
        }

        User user = mapper.map(newUser, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        user = userRepository.saveAndFlush(user);

        UserDto dto =  mapper.map(user, UserDto.class);
        return user;
    }
    
    public TokenDto login(LoginDto loginDto,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String e = new String("Invalid login data ");

            throw new InvalidUserDataException(e);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findUserByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new InvalidUserDataException("User not found"));

            return new TokenDto(jwtUtil.generateToken(userDetails),tokenService.generateRefreshToken(user));

        } catch (AuthenticationException e) {
            throw new InvalidUserDataException("Invalid email or password.");
        }
    }


    public String logout(TokenDto tokenDto) {
        Function<Claims, Date> extractEXpire = Claims::getExpiration;

        Date expiration = jwtUtil.extractClaim(tokenDto.getAccessToken(),extractEXpire);
        Date now = new Date();
        long diffInMillis = expiration.getTime() - now.getTime();

        int timeToExpire = (int) TimeUnit.MILLISECONDS.toSeconds(diffInMillis);

        redisService.storeValue(TOKEN_PREFIX+tokenDto.getAccessToken() ,tokenDto.getAccessToken(),timeToExpire);
        return "Logout Succeeds";
    }
}
