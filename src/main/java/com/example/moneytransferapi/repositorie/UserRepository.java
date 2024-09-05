package com.example.moneytransferapi.repositorie;

import com.example.moneytransferapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.accounts WHERE u.id = :userId")
    User findUserWithAccountsById(int userId);

}
