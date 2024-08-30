package com.example.moneytransferapi.utilitys;

import com.example.moneytransferapi.entity.User;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {


    @Value("${token.expirationTime-minutes}")
    private long expirationTimeMinutes;
    private SecretKey key;

    @Value("${key}")
    private String secretKey;

    @PostConstruct
    public void buildKey() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(UserDetails userDetails){
        long expirationTimeInMillis = expirationTimeMinutes * 60 * 1000;

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
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



}
