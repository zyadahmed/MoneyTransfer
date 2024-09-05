package com.example.moneytransferapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "favourites")
@IdClass(FavouriteId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favourite {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;
}
