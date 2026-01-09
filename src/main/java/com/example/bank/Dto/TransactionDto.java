package com.example.bank.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

    private String transactionId;
    private String fromAccount;
    private String toAccount;
    private Double amount;
    private String transactionType;
    private String transactionDate;
}
