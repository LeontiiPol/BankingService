package com.example.bankingservice.service.account;

import com.example.bankingservice.dto.AccountDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {

   AccountDto createAccount(AccountDto data);
   AccountDto find(UUID number);
   List<AccountDto> findAll();
   AccountDto updateBalance(UUID number, BigDecimal balance);
}
