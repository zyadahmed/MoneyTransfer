package com.example.moneytransferapi.repositorie;

import com.example.moneytransferapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrascationRepository extends JpaRepository<Transaction,Long> {

}
