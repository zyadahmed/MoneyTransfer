package com.example.moneytransferapi.utilitys;

import com.example.moneytransferapi.entity.User;
import com.example.moneytransferapi.exception.InvalidUserDataException;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {


    @Value("${token.expirationTime-minutes}")
    private long expirationTimeMinutes;
    private SecretKey key;
    private final UserRepository userRepository;

    @Value("${key}")
    private String secretKey;

    @PostConstruct
    public void buildKey() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(UserDetails userDetails){
        Optional<User> currentUser = userRepository.findUserByEmail(userDetails.getUsername());
        int id;
        if (currentUser.isPresent()){
            id = currentUser.get().getId();
        }else {
            throw new InvalidUserDataException("User not exist");
        }
        long expirationTimeInMillis = expirationTimeMinutes * 60 * 1000;
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .claim("id",id)
                .claim("Role",userDetails.getAuthorities())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(key)
                .compact();

    }
    public String generateRefreshToken(User user){
        return Jwts.builder()
                .subject(String.valueOf(user.getEmail()))
                .signWith(key)
                .compact();
    }
    public boolean isValid(String accessToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            return !claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsTFunction){
         Claims claims = getClaims(token);
        return claimsTFunction.apply(claims);
    }
    public Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("id", Integer.class));
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }




}
