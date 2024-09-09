package com.example.moneytransferapi.dto;

import java.util.Date;
import java.util.List;

public class UserResponseDto {
    private int id;
    private String name;
    private String email;
    private String country;

    private Date dateofbirth;
    private List<AccountDTO> accounts;

}
