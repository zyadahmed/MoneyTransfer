package com.example.moneytransferapi.entity;

import com.example.moneytransferapi.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "transcations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transcation_id")
    private int id;

    @Column(nullable = false)
    private Long amount;


    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "created_timeStamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdTimeStamp;

    @ManyToOne
    @JoinColumn(name = "sender_account_id", nullable = false)
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "recieve_account_id", nullable = false)
    private Account receiverAccount;


}
