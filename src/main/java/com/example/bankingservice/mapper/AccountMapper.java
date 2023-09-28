package com.example.bankingservice.mapper;

import com.example.bankingservice.dto.AccountDto;
import com.example.bankingservice.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

   @Mapping(target = "beneficiaryName", expression = "java(account.getBeneficiary().getName())")
   AccountDto accountToAccountDto(Account account);
}
