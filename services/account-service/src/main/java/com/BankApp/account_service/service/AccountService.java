package com.BankApp.account_service.service;

import com.BankApp.account_service.dto.AccountDtoRequest;
import com.BankApp.account_service.dto.AccountDtoResponse;

import java.util.List;

public interface AccountService {
    AccountDtoResponse createAccount(AccountDtoRequest accoutDtoRequest);
    AccountDtoResponse getAccount(Long id);
    List<AccountDtoResponse> getAllAccounts();
    void deleteAccount(Long id);
}
