package com.BankApp.account_service.exception;

public class ClientServiceException extends RuntimeException {
    public ClientServiceException(Long id) {
        super("Client with id " + id + " not found");
    }
}