package com.BankApp.customer_service.service;

import com.BankApp.customer_service.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto saveCustomer(CustomerDto customerDTO);
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(Long id);
    CustomerDto getCustomerByEmail(String email);
}
