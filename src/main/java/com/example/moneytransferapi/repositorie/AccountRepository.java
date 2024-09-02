package com.example.moneytransferapi.repositorie;

import com.example.moneytransferapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByUserId(int userId);
    List<Account> findByUser_Email(String email);
}
