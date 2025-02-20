package com.BankApp.account_service.service.impl;

import com.BankApp.account_service.dto.AccountDtoRequest;
import com.BankApp.account_service.dto.AccountDtoResponse;
import com.BankApp.account_service.entity.Account;
import com.BankApp.account_service.exception.AccountNotFoundException;
import com.BankApp.account_service.exception.ClientServiceException;
import com.BankApp.account_service.exception.ResourceNotFoundException;
import com.BankApp.account_service.mapper.AccountMapper;
import com.BankApp.account_service.repository.AccountRepository;
import com.BankApp.account_service.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final RestTemplate restTemplate;

    @Override
    public AccountDtoResponse getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException(id));
        return accountMapper.toResponseDto(account);
    }

    @Override
    public List<AccountDtoResponse> getAllAccounts() {
        return accountMapper.toAccountDTOs(accountRepository.findAll());
    }

    @Override
    public AccountDtoResponse createAccount(AccountDtoRequest accountDtoRequest) {
        Account account = accountMapper.toAccount(accountDtoRequest);
        log.info("Checking if client with ID {} exists...", account.getClientId());

        if (!clientExists(account.getClientId())) {
            throw new ResourceNotFoundException("Le client avec ID " + account.getClientId() + " n'existe pas");
        }

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponseDto(savedAccount);

    }

    @Override
    public void deleteAccount(Long id){
        Account account = accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException(id));
    }


    public boolean clientExists(Long clientId) {
        String url = "http://localhost:8080/CUSTOMER-SERVICE/customers/" + clientId;
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    url, HttpMethod.GET,null,Void.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Client with ID {} not found.", clientId);
            return false;
        } catch (HttpClientErrorException e) {
            log.error("HTTP error while checking client existence: {}", e.getMessage());
            throw new ClientServiceException("Erreur lors de la vérification du client: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while checking if client exists", e);
            throw new ClientServiceException("Erreur interne lors de la vérification du client.");
        }
    }

}
