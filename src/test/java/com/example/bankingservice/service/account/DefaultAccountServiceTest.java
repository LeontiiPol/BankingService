package com.example.bankingservice.service.account;

import com.example.bankingservice.dao.AccountRepository;
import com.example.bankingservice.dto.AccountDto;
import com.example.bankingservice.exception.BankServiceBadRequestException;
import com.example.bankingservice.mapper.AccountMapper;
import com.example.bankingservice.model.Account;
import com.example.bankingservice.model.Beneficiary;
import com.example.bankingservice.service.beneficiary.BeneficiaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.example.bankingservice.service.account.DefaultAccountService.ACCOUNT_NOT_FND_MSG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAccountServiceTest {

   @Mock
   private AccountRepository repository;
   @Mock
   private BeneficiaryService beneficiaryService;
   @Mock
   private AccountMapper mapper;
   @Mock
   private PasswordEncoder passwordEncoder;
   @InjectMocks
   private DefaultAccountService defaultAccountService;
   @Captor
   ArgumentCaptor<Account> accountCaptor;

   @Test
   void createAccountTest() {
      AccountDto accountDto = AccountDto.builder().beneficiaryName("Nick").pin("1234").build();
      Beneficiary beneficiary = new Beneficiary(UUID.randomUUID(), accountDto.getBeneficiaryName());
      ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
      String encodedPin = "4321";

      when(beneficiaryService.findByName(accountDto.getBeneficiaryName())).thenReturn(beneficiary);
      when(passwordEncoder.encode(accountDto.getPin())).thenReturn(encodedPin);

      defaultAccountService.createAccount(accountDto);

      verify(repository).save(accountCaptor.capture());
      Account result = accountCaptor.getValue();
      assertEquals("Nick", result.getBeneficiary().getName());
      assertEquals(encodedPin, result.getPin());
   }

   @Test
   void find_whenAccountExists_thenReturnsDto() {
      Account account = Account.builder().number(UUID.randomUUID()).build();
      when(repository.findById(account.getNumber())).thenReturn(Optional.of(account));

      defaultAccountService.find(account.getNumber());

      verify(mapper).accountToAccountDto(accountCaptor.capture());
      assertEquals(account, accountCaptor.getValue());
   }

   @Test
   void find_whenAccountNotExists_thenThrowsException() {
      Account account = Account.builder().number(UUID.randomUUID()).build();
      when(repository.findById(account.getNumber())).thenReturn(Optional.empty());

      assertThrows(
          BankServiceBadRequestException.class,
          () -> defaultAccountService.find(account.getNumber()),
          String.format(ACCOUNT_NOT_FND_MSG, account.getNumber())
      );
   }

   @Test
   void updateBalance_whenAccountExists_updatesBalance() {
      Account account = Account.builder().balance(BigDecimal.ONE).number(UUID.randomUUID()).build();
      when(repository.findById(account.getNumber())).thenReturn(Optional.of(account));

      defaultAccountService.updateBalance(account.getNumber(), BigDecimal.TEN);

      verify(repository).save(accountCaptor.capture());
      Account result = accountCaptor.getValue();
      assertAll(
          () -> assertEquals(account.getNumber(), result.getNumber()),
          () -> assertEquals(account.getBalance(), result.getBalance())
      );
   }
}