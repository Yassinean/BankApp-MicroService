package com.BankApp.customer_service;

import com.BankApp.customer_service.dto.CustomerDtoRequest;
import com.BankApp.customer_service.dto.CustomerDtoResponse;
import com.BankApp.customer_service.entity.Customer;
import com.BankApp.customer_service.exception.CustomerNotFoundException;
import com.BankApp.customer_service.exception.EmailCustomerNotFoundException;
import com.BankApp.customer_service.mapper.CustomerMapper;
import com.BankApp.customer_service.repository.CustomerRepository;
import com.BankApp.customer_service.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private CustomerMapper customerMapper;

	@InjectMocks
	private CustomerServiceImpl customerService;

	private Customer customer;
	private CustomerDtoRequest customerDtoRequest;
	private CustomerDtoResponse customerDtoResponse;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		customer = new Customer();
		customer.setId(1L);
		customer.setName("John Doe");
		customer.setEmail("john.doe@example.com");

		customerDtoRequest = new CustomerDtoRequest();
		customerDtoRequest.setName("John Doe");
		customerDtoRequest.setEmail("john.doe@example.com");

		customerDtoResponse = new CustomerDtoResponse();
		customerDtoResponse.setId(1L);
		customerDtoResponse.setName("John Doe");
		customerDtoResponse.setEmail("john.doe@example.com");
	}

	@Test
	void testSaveCustomer_Success() {
		// Arrange
		when(customerMapper.toCustomer(customerDtoRequest)).thenReturn(customer);
		when(customerRepository.save(customer)).thenReturn(customer);
		when(customerMapper.toResponseDto(customer)).thenReturn(customerDtoResponse);

		// Act
		CustomerDtoResponse result = customerService.saveCustomer(customerDtoRequest);

		// Assert
		assertNotNull(result);
		assertEquals("John Doe", result.getName());
		assertEquals("john.doe@example.com", result.getEmail());

		verify(customerRepository, times(1)).save(customer);
	}

	@Test
	void testSaveCustomer_Failure() {
		// Arrange
		when(customerMapper.toCustomer(customerDtoRequest)).thenReturn(customer);
		when(customerRepository.save(customer)).thenThrow(new RuntimeException("Error saving customer"));

		// Act & Assert
		Exception exception = assertThrows(RuntimeException.class, () -> customerService.saveCustomer(customerDtoRequest));
		assertEquals("Error saving customer", exception.getMessage());

		verify(customerRepository, times(1)).save(customer);
	}

	@Test
	void testGetCustomerById_Success() {
		// Arrange
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(customerMapper.toResponseDto(customer)).thenReturn(customerDtoResponse);

		// Act
		CustomerDtoResponse result = customerService.getCustomerById(1L);

		// Assert
		assertNotNull(result);
		assertEquals("John Doe", result.getName());

		verify(customerRepository, times(1)).findById(1L);
	}

	@Test
	void testGetCustomerById_NotFound() {
		// Arrange
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));

		verify(customerRepository, times(1)).findById(1L);
	}

	@Test
	void testGetCustomerByEmail_Success() {
		// Arrange
		when(customerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(customer));
		when(customerMapper.toResponseDto(customer)).thenReturn(customerDtoResponse);

		// Act
		CustomerDtoResponse result = customerService.getCustomerByEmail("john.doe@example.com");

		// Assert
		assertNotNull(result);
		assertEquals("John Doe", result.getName());
		assertEquals("john.doe@example.com", result.getEmail());

		verify(customerRepository, times(1)).findByEmail("john.doe@example.com");
	}

	@Test
	void testGetCustomerByEmail_NotFound() {
		// Arrange
		when(customerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(EmailCustomerNotFoundException.class, () -> customerService.getCustomerByEmail("john.doe@example.com"));

		verify(customerRepository, times(1)).findByEmail("john.doe@example.com");
	}

	@Test
	void testGetAllCustomers_Success() {
		// Arrange
		List<Customer> customers = List.of(customer);
		List<CustomerDtoResponse> customerDtoResponses = List.of(customerDtoResponse);

		when(customerRepository.findAll()).thenReturn(customers);
		when(customerMapper.toCustomerDTOs(customers)).thenReturn(customerDtoResponses);

		// Act
		List<CustomerDtoResponse> result = customerService.getAllCustomers();

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("John Doe", result.get(0).getName());

		verify(customerRepository, times(1)).findAll();
	}

	@Test
	void testGetAllCustomers_Empty() {
		// Arrange
		when(customerRepository.findAll()).thenReturn(List.of());

		// Act
		List<CustomerDtoResponse> result = customerService.getAllCustomers();

		// Assert
		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(customerRepository, times(1)).findAll();
	}
}
