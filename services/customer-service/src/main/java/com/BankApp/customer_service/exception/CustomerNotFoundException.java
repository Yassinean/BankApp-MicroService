package com.BankApp.customer_service.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
