package com.BankApp.customer_service.exception;

public class EmailCustomerNotFoundException extends RuntimeException {
    public EmailCustomerNotFoundException(String email) {
        super("Customer with email " + email + " not found");
    }
}
