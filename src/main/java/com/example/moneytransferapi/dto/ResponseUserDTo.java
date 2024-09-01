package com.example.moneytransferapi.dto;


import lombok.Data;

@Data
public class ResponseUserDTo {
    private int id;
    private String name;
    private String email;
    private String country;

}
