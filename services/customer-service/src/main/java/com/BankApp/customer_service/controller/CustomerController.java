package com.BankApp.customer_service.controller;

import com.BankApp.customer_service.dto.CustomerDtoRequest;
import com.BankApp.customer_service.dto.CustomerDtoResponse;
import com.BankApp.customer_service.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerDtoResponse> createCustomer(@Valid @RequestBody CustomerDtoRequest customerDTO) {
        return ResponseEntity.ok(service.saveCustomer(customerDTO));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDtoResponse>> getAllCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDtoResponse> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCustomerById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDtoResponse> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getCustomerByEmail(email));
    }
}