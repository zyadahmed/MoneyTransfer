package com.example.moneytransferapi.repositorie;

import com.example.moneytransferapi.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrascationRepository extends JpaRepository<Transaction,Long> {

    Page<Transaction> findAllBySenderAccountIdOrReceiverAccountIdOrderByCreatedTimeStampDesc(Long senderId, Long receiverId, Pageable pageable);


}
