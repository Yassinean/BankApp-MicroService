package com.BankApp.customer_service.service.impl;

import com.BankApp.customer_service.dto.CustomerDtoRequest;
import com.BankApp.customer_service.dto.CustomerDtoResponse;
import com.BankApp.customer_service.entity.Customer;
import com.BankApp.customer_service.exception.CustomerNotFoundException;
import com.BankApp.customer_service.exception.EmailCustomerNotFoundException;
import com.BankApp.customer_service.mapper.CustomerMapper;
import com.BankApp.customer_service.repository.CustomerRepository;
import com.BankApp.customer_service.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CustomerDtoResponse saveCustomer(CustomerDtoRequest customerDto) {
        Customer customer = mapper.toCustomer(customerDto);
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        Customer savedCustomer = repository.save(customer);
        return mapper.toResponseDto(savedCustomer);
    }

    @Override
    public List<CustomerDtoResponse> getAllCustomers() {
        return mapper.toCustomerDTOs(repository.findAll());
    }

    @Override
    public CustomerDtoResponse getCustomerById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public CustomerDtoResponse getCustomerByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EmailCustomerNotFoundException("Customer with email " + email + " not found"));
    }
}
