package com.BankApp.customer_service.mapper;

import com.BankApp.customer_service.dto.CustomerDtoRequest;
import com.BankApp.customer_service.dto.CustomerDtoResponse;
import com.BankApp.customer_service.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toCustomer(CustomerDtoRequest customerRequest);
    CustomerDtoResponse toResponseDto(Customer customer);
    List<CustomerDtoResponse> toCustomerDTOs(List<Customer> customers);
}
