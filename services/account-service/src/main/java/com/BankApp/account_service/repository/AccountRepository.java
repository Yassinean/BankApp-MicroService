package com.BankApp.account_service.repository;

import com.BankApp.account_service.entity.Account;
import com.BankApp.account_service.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByClientIdAndType(Long id ,AccountType type);
    List<Account> findByClientId(Long clientId);
}
