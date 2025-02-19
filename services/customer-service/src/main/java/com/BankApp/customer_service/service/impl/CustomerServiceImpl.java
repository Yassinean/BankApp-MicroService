package com.BankApp.customer_service.service.impl;

import com.BankApp.customer_service.dto.CustomerDto;
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
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        Customer customer = mapper.toCustomer(customerDto);
        return mapper.toCustomerDTO(repository.save(customer));
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return mapper.toCustomerDTOs(repository.findAll());
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        return repository.findById(id)
                .map(mapper::toCustomerDTO)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public CustomerDto getCustomerByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toCustomerDTO)
                .orElseThrow(() -> new EmailCustomerNotFoundException("Customer with email " + email + " not found"));
    }
}
