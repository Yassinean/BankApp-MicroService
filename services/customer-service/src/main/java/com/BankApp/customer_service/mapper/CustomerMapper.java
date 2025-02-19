package com.BankApp.customer_service.mapper;

import com.BankApp.customer_service.dto.CustomerDto;
import com.BankApp.customer_service.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto toCustomerDTO(Customer customer);
    Customer toCustomer(CustomerDto customerDTO);
    List<CustomerDto> toCustomerDTOs(List<Customer> customers);
}
