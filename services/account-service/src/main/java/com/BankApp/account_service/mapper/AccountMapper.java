package com.BankApp.account_service.mapper;


import com.BankApp.account_service.dto.AccountDtoRequest;
import com.BankApp.account_service.dto.AccountDtoResponse;
import com.BankApp.account_service.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AccountDtoRequest accountRequest);
    AccountDtoResponse toResponseDto(Account account);
    List<AccountDtoResponse> toAccountDTOs(List<Account> accounts);
}
