package com.example.bankingservice.service.account;

import com.example.bankingservice.dao.AccountRepository;
import com.example.bankingservice.dto.AccountDto;
import com.example.bankingservice.exception.BankServiceBadRequestException;
import com.example.bankingservice.mapper.AccountMapper;
import com.example.bankingservice.model.Account;
import com.example.bankingservice.service.beneficiary.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DefaultAccountService implements AccountService {

   public static final String ACCOUNT_NOT_FND_MSG = "Account with number %s not found";

   private final AccountRepository repository;
   private final BeneficiaryService beneficiaryService;
   private final AccountMapper mapper;
   private final PasswordEncoder passwordEncoder;

   @Override
   public AccountDto createAccount(AccountDto data) {
      Account account = Account.builder()
          .beneficiary(beneficiaryService.findByName(data.getBeneficiaryName()))
          .pin(passwordEncoder.encode(data.getPin()))
          .balance(BigDecimal.ZERO)
          .build();
      return mapper.accountToAccountDto(repository.save(account));
   }

   @Override
   public AccountDto find(UUID number) {
      return mapper.accountToAccountDto(findModel(number));
   }

   private Account findModel(UUID number) {
      return repository.findById(number)
          .orElseThrow(() -> new BankServiceBadRequestException(String.format(ACCOUNT_NOT_FND_MSG, number)));
   }

   @Override
   public List<AccountDto> findAll() {
      return repository.findAll()
          .stream()
          .map(mapper::accountToAccountDto)
          .toList();
   }

   @Override
   public AccountDto updateBalance(UUID number, BigDecimal balance) {
      Account account = findModel(number);
      account.setBalance(balance);
      return mapper.accountToAccountDto(repository.save(account));
   }
}
