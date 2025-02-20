package com.BankApp.customer_service.service;

import com.BankApp.customer_service.dto.CustomerDtoRequest;
import com.BankApp.customer_service.dto.CustomerDtoResponse;

import java.util.List;

public interface CustomerService {
    CustomerDtoResponse saveCustomer(CustomerDtoRequest customerDTO);
    List<CustomerDtoResponse> getAllCustomers();
    CustomerDtoResponse getCustomerById(Long id);
    CustomerDtoResponse getCustomerByEmail(String email);
}
