package com.example.moneytransferapi.repositorie;

import com.example.moneytransferapi.entity.RefreshToken;
import com.example.moneytransferapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.usedToken = true WHERE rt.user = :user")
    void markAllTokensAsUsed(User user);

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user.email = :email")
    List<RefreshToken> findAllByUserEmail(String email);

}
