package com.BankApp.account_service;

import com.BankApp.account_service.dto.AccountDtoRequest;
import com.BankApp.account_service.dto.AccountDtoResponse;
import com.BankApp.account_service.entity.Account;
import com.BankApp.account_service.entity.AccountType;
import com.BankApp.account_service.exception.ClientServiceException;
import com.BankApp.account_service.exception.ResourceNotFoundException;
import com.BankApp.account_service.mapper.AccountMapper;
import com.BankApp.account_service.repository.AccountRepository;
import com.BankApp.account_service.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {
	@Mock
	private AccountRepository accountRepository;

	@Mock
	private AccountMapper accountMapper;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private AccountServiceImpl accountService;

	private Account account;
	private AccountDtoRequest accountDtoRequest;
	private AccountDtoResponse accountDtoResponse;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		account = new Account();
		account.setId(1L);
		account.setClientId(100L);
		account.setType(AccountType.EPARGNE);

		accountDtoRequest = AccountDtoRequest.builder()
				.balance(4000.00)
				.type(AccountType.EPARGNE)
				.clientId(100L)
				.build();
		accountDtoResponse = new AccountDtoResponse();
		accountDtoResponse.setId(1L);
		accountDtoResponse.setClientId(100L);
		accountDtoResponse.setType(AccountType.EPARGNE);
	}

	@Test
	void testCreateAccount_Success() {
		// Arrange
		when(accountMapper.toAccount(accountDtoRequest)).thenReturn(account);
		when(accountRepository.existsByClientIdAndType(100L, AccountType.EPARGNE)).thenReturn(false);
		when(accountRepository.save(account)).thenReturn(account);
		when(accountMapper.toResponseDto(account)).thenReturn(accountDtoResponse);

		// Mocking exchange method to return a valid ResponseEntity
		String url = "http://localhost:8080/customer-service/customers/100";
		when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), eq(Void.class)))
				.thenReturn(ResponseEntity.ok().build()); // Mock a valid response

		// Act
		AccountDtoResponse result = accountService.createAccount(accountDtoRequest);

		// Assert
		assertNotNull(result);
		assertEquals(AccountType.EPARGNE, result.getType());

		verify(accountRepository, times(1)).save(account);
	}

	@Test
	void testCreateAccount_ClientNotFound() {
		// Arrange
		when(accountMapper.toAccount(accountDtoRequest)).thenReturn(account);
		String url = "http://localhost:8080/customer-service/customers/100";
		when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), eq(Void.class)))
				.thenReturn(ResponseEntity.badRequest().build());

		// Act & Assert
		assertFalse(accountService.clientExists(100L));  // Validate client existence

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(accountDtoRequest));
		assertEquals("Le client avec ID 100 n'existe pas", exception.getMessage());

		verify(accountRepository, never()).save(any());
	}

	@Test
	void testCreateAccount_AlreadyExists() {
		// Arrange
		when(accountMapper.toAccount(accountDtoRequest)).thenReturn(account);
		String url = "http://localhost:8080/customer-service/customers/100";
		when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), eq(Void.class)))
				.thenReturn(ResponseEntity.ok().build());

		// Mock client exists validation
		assertTrue(accountService.clientExists(100L));

		// Simulate the account already exists for this client and type
		when(accountRepository.existsByClientIdAndType(100L, AccountType.EPARGNE)).thenReturn(true);

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(accountDtoRequest));
		assertEquals("Un client ne peut avoir qu'un seul compte de type EPARGNE", exception.getMessage());

		verify(accountRepository, never()).save(any());
	}

	@Test
	void testGetAccountById_Success() {
		// Arrange
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		when(accountMapper.toResponseDto(account)).thenReturn(accountDtoResponse);

		// Act
		AccountDtoResponse result = accountService.getAccount(1L);

		// Assert
		assertNotNull(result);
		assertEquals(AccountType.EPARGNE, result.getType());

		verify(accountRepository, times(1)).findById(1L);
	}

	@Test
	void testGetAccountById_NotFound() {
		// Arrange
		when(accountRepository.findById(1L)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> accountService.getAccount(1L));

		verify(accountRepository, times(1)).findById(1L);
	}

	@Test
	void testGetAccountsByClientId_Success() {
		// Arrange
		List<Account> accounts = List.of(account);
		List<AccountDtoResponse> accountDtoResponses = List.of(accountDtoResponse);

		when(accountRepository.findByClientId(100L)).thenReturn(accounts);
		when(accountMapper.toAccountDTOs(accounts)).thenReturn(accountDtoResponses);

		// Act
		List<AccountDtoResponse> result = accountService.getAllAccounts();

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(AccountType.EPARGNE, result.get(0).getType());

		verify(accountRepository, times(1)).findByClientId(100L);
	}

	@Test
	void testClientExists_Success() {
		// Arrange
		String url = "http://localhost:8080/customer-service/customers/100";
		when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), eq(Void.class)))
				.thenReturn(ResponseEntity.ok().build());

		// Act
		boolean result = accountService.clientExists(100L);

		// Assert
		assertTrue(result);
	}

	@Test
	void testClientExists_NotFound() {
		// Arrange
		String url = "http://localhost:8080/customer-service/customers/100";
		when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), eq(Void.class)))
				.thenThrow(HttpClientErrorException.NotFound.class);

		// Act
		boolean result = accountService.clientExists(100L);

		// Assert
		assertFalse(result);
	}

	@Test
	void testClientExists_ClientServiceException() {
		// Arrange
		String url = "http://localhost:8080/customer-service/customers/100";
		when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), eq(Void.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

		// Act & Assert
		assertThrows(ClientServiceException.class, () -> accountService.clientExists(100L));
	}
}
