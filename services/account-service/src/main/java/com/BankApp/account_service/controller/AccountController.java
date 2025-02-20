package com.BankApp.account_service.controller;

import com.BankApp.account_service.dto.AccountDtoRequest;
import com.BankApp.account_service.dto.AccountDtoResponse;
import com.BankApp.account_service.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService service;

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AccountDtoResponse>> getAllAccounts() {
        return ResponseEntity.ok(service.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDtoResponse> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAccount(id));
    }

    @PostMapping
    public ResponseEntity<AccountDtoResponse> createAccount(@Valid @RequestBody AccountDtoRequest customerDTO) {
        return ResponseEntity.ok(service.createAccount(customerDTO));
    }
}
