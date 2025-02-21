package com.BankApp.account_service.dto;

import com.BankApp.account_service.entity.AccountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class AccountDtoRequest {
    @NotNull(message = "Balance is required.")
    @Min(value = 0, message = "Balance must be at least 0.")
    private Double balance;

    @NotNull(message = "Account type is required.")
    private AccountType type;

    @NotNull(message = "Client ID is required.")
    private Long clientId;
}
