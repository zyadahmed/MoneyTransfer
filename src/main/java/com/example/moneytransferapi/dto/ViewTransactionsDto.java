package com.example.moneytransferapi.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ViewTransactionsDto {

    Long accountId;
    int page;
    int size;
}
