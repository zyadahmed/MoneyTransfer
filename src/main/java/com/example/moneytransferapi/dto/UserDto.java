package com.example.moneytransferapi.dto;

import com.example.moneytransferapi.entity.Account;
import com.example.moneytransferapi.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.List;

public class UserDto {
    private int id;
    private String name;
    private String email;
    private String country;

    private Date dateofbirth;


    private List<Account> accounts;


}
