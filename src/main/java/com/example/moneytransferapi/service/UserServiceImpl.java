package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.*;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.enums.Role;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.exception.UserNotFoundException;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.securityconfig.JwtAuthFIlter;
import com.example.moneytransferapi.utilitys.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{


    private RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final TokenService tokenService;
    private static final String TOKEN_PREFIX = "b:";



    @Transactional
    public ResponseUserDTo createUser(RegistrationDto newUser) {

        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new InvalidUserDataException("Email already exists.");
        }
        User user = mapper.map(newUser, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        user = userRepository.saveAndFlush(user);
        return mapper.map(user, ResponseUserDTo.class);
    }
    
    public TokensDto login(LoginDto loginDto) {
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

            return new TokensDto(jwtUtil.generateToken(userDetails),tokenService.generateRefreshToken(user));

        } catch (AuthenticationException e) {
            throw new InvalidUserDataException("Invalid email or password.");
        }
    }

    public String logout(TokensDto tokenDto) {
        Function<Claims, Date> extractExpire = Claims::getExpiration;

        Date expiration = jwtUtil.extractClaim(tokenDto.getAccessToken(),extractExpire);
        Date now = new Date();
        long diffInMillis = expiration.getTime() - now.getTime();

        int timeToExpire = (int) TimeUnit.MILLISECONDS.toSeconds(diffInMillis);

        redisService.storeValue(TOKEN_PREFIX+tokenDto.getAccessToken() ,tokenDto.getAccessToken(),timeToExpire);
        return "Logout Succeeds";
    }

    @Override
    @Transactional
    public String updatePassword(UpdatePasswordDto updatePasswordDto, HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        int currentUserId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException("User Not found"));

        if (!passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())) {
            throw new InvalidUserDataException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));

        userRepository.save(user);

        return "Password updated successfully";

    }

    @Override
    public User getUserWithAccounts(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        int currentUserId = jwtUtil.extractUserId(token);
        return userRepository.findUserWithAccountsById(currentUserId);
    }
}
