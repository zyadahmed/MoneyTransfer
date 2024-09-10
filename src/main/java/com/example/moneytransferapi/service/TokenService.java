package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.TokensDto;
import com.example.moneytransferapi.entity.RefreshToken;
import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.exception.InvalidRefreshTokenException;
import com.example.moneytransferapi.repositorie.RefreshTokenRepository;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.security.UserSecurityAdapter;
import com.example.moneytransferapi.utilitys.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtService;
    private final SessionService sessionService;

    public String generateRefreshToken(User user) {
        String rawRefreshToken = jwtService.generateRefreshToken(user);
        String encodedRefreshToken = passwordEncoder.encode(rawRefreshToken);

        RefreshToken token = new RefreshToken();
        token.setToken(encodedRefreshToken);
        token.setUser(user);
        token.setExpireDate(LocalDateTime.now().plusDays(7));
        token.setUsedToken(false);
        refreshTokenRepository.save(token);

        return rawRefreshToken;
    }

    public Optional<RefreshToken> validRefreshToken(String refreshToken) {
        String userMail = jwtService.extractClaim(refreshToken, Claims::getSubject);
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserEmail(userMail);
        for (RefreshToken token : tokens) {
            if (passwordEncoder.matches(refreshToken, token.getToken())) {
                if (token.getExpireDate().isAfter(LocalDateTime.now()) && !token.isUsedToken()) {
                    return Optional.of(token);
                }
            }
        }
        return Optional.empty();
    }



    public User getUserFromRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void markTokenAsUsed(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(token -> {
            token.setUsedToken(true);
            refreshTokenRepository.save(token);
        });
    }

    public void markAllTokensAsUsed(User user) {
        refreshTokenRepository.markAllTokensAsUsed(user);
    }

    public TokensDto refreshAccessToken(String refreshToken) {
        Optional<RefreshToken> savedrefreshTokenOptional= validRefreshToken(refreshToken);
        if (savedrefreshTokenOptional.isPresent()) {

            User user = getUserFromRefreshToken(savedrefreshTokenOptional.get().getToken());

            markTokenAsUsed(savedrefreshTokenOptional.get().getToken());
            String newAccessToken = jwtService.generateToken(new UserSecurityAdapter(user));
            String newRefreshToken = generateRefreshToken(user);
            sessionService.createSession(newAccessToken);

            return new TokensDto(newAccessToken, newRefreshToken);
        } else {
            User user = getUserFromRefreshToken(refreshToken);
            markAllTokensAsUsed(user);
            throw new InvalidRefreshTokenException("Invalid or expired refresh token");
        }
    }
}
