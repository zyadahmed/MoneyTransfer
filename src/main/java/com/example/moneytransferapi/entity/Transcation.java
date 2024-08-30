package com.example.moneytransferapi.entity;

import com.example.moneytransferapi.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transcations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transcation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transcation_id")
    private int id;

    @Column(nullable = false)
    private Long amoumt;
    private String type;


    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "created_timeStamp", nullable = false)
    private LocalDateTime createdTimeStamp;

    @ManyToOne
    @JoinColumn(name = "sender_account_id", nullable = false)
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "recieve_account_id", nullable = false)
    private Account receiverAccount;


}
