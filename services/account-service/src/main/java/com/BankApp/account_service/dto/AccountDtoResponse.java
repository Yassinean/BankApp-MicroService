package com.BankApp.account_service.dto;

import com.BankApp.account_service.entity.AccountType;
import lombok.Data;

@Data
public class AccountDtoResponse {
    private Long id;
    private Double balance;
    private AccountType type;
    private Long clientId;
}
