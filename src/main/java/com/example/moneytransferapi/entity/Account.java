package com.example.moneytransferapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1,initialValue=10000000)
    @Column(name = "account_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "account_type")
    private String type;

    @Column(nullable = false)
    private Long balance;


    @Column(nullable = false)
    @CreationTimestamp
    private Date created_at;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "senderAccount")
    @JsonIgnore
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiverAccount")
    @JsonIgnore
    private List<Transaction> receivedTransactions;


}
