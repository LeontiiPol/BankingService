package com.example.bankingservice.service.transactions;

import com.example.bankingservice.dao.TransactionRepository;
import com.example.bankingservice.dto.AccountDto;
import com.example.bankingservice.dto.TransactionDto;
import com.example.bankingservice.dto.TransactionOperation;
import com.example.bankingservice.exception.BankServiceBadRequestException;
import com.example.bankingservice.mapper.TransactionMapper;
import com.example.bankingservice.model.Transaction;
import com.example.bankingservice.service.account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.example.bankingservice.service.transactions.DefaultTransactionService.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTransactionServiceTest {

   @Mock
   private AccountService accountService;
   @Mock
   private TransactionRepository transactionRepository;
   @Mock
   private PasswordEncoder encoder;
   @Mock
   private TransactionMapper transactionMapper;
   @InjectMocks
   private DefaultTransactionService transactionService;
   @Captor
   private ArgumentCaptor<Transaction> transactionCaptor;
   @Captor
   private ArgumentCaptor<BigDecimal> newBalanceCaptor;
   private AccountDto richAccount;
   private AccountDto poorAccount;
   private TransactionDto transactionDto;

   @BeforeEach
   void setUp() {
      richAccount = AccountDto.builder()
          .number(UUID.randomUUID())
          .balance(BigDecimal.TEN)
          .build();

      poorAccount = AccountDto.builder()
          .number(UUID.randomUUID())
          .balance(BigDecimal.ZERO)
          .build();

      transactionDto = TransactionDto.builder()
          .sum(BigDecimal.ONE)
          .receiver(poorAccount.getNumber())
          .build();
   }

   @Test
   void deposit_whenPinCorrect_thenDepositSucceed() {
      when(accountService.find(richAccount.getNumber())).thenReturn(richAccount);
      when(encoder.matches(null, null)).thenReturn(true);
      when(transactionMapper.transactionToTransactionDto(null)).thenReturn(transactionDto);

      transactionService.deposit(richAccount.getNumber(), transactionDto);

      verify(accountService).updateBalance(ArgumentMatchers.any(), newBalanceCaptor.capture());
      verify(transactionRepository).save(transactionCaptor.capture());
      Transaction transaction = transactionCaptor.getValue();

      assertAll(
          () -> assertEquals(richAccount.getNumber(), transaction.getAccount()),
          () -> assertEquals(TransactionOperation.DEPOSIT, transaction.getOperation()),
          () -> assertEquals(BigDecimal.ONE, transaction.getSum()),
          () -> assertNotNull(transaction.getTimestamp()),
          () -> assertEquals(BigDecimal.valueOf(11), newBalanceCaptor.getValue())
      );
   }

   @Test
   void deposit_whenPinIncorrect_thenExceptionThrown() {
      when(accountService.find(richAccount.getNumber())).thenReturn(richAccount);
      when(encoder.matches(null, null)).thenReturn(false);

      assertThrows(
          BankServiceBadRequestException.class,
          () -> transactionService.deposit(richAccount.getNumber(), transactionDto),
          INCORRECT_PIN_MSG
      );
   }

   @Test
   void withdraw_whenPinCorrectAndSufficientFunds_thenWithdrawSucceed() {
      when(accountService.find(richAccount.getNumber())).thenReturn(richAccount);
      when(encoder.matches(null, null)).thenReturn(true);
      when(transactionMapper.transactionToTransactionDto(null)).thenReturn(transactionDto);

      transactionService.withdraw(richAccount.getNumber(), transactionDto);

      verify(accountService).updateBalance(ArgumentMatchers.any(), newBalanceCaptor.capture());
      verify(transactionRepository).save(transactionCaptor.capture());
      Transaction transaction = transactionCaptor.getValue();
      assertAll(
          () -> assertEquals(richAccount.getNumber(), transaction.getAccount()),
          () -> assertEquals(TransactionOperation.WITHDRAW, transaction.getOperation()),
          () -> assertEquals(BigDecimal.ONE, transaction.getSum()),
          () -> assertNotNull(transaction.getTimestamp()),
          () -> assertEquals(BigDecimal.valueOf(9), newBalanceCaptor.getValue())
      );
   }

   @Test
   void withdraw_whenPinIncorrectAndSufficientFunds_thenThrowsException() {
      when(accountService.find(richAccount.getNumber())).thenReturn(richAccount);
      when(encoder.matches(null, null)).thenReturn(false);

      assertThrows(
          BankServiceBadRequestException.class,
          () -> transactionService.withdraw(richAccount.getNumber(), transactionDto),
          INCORRECT_PIN_MSG
      );
   }

   @Test
   void withdraw_whenPinCorrectAndInsufficientFunds_thenThrowsException() {
      when(accountService.find(poorAccount.getNumber())).thenReturn(poorAccount);
      when(encoder.matches(null, null)).thenReturn(true);

      assertThrows(
          BankServiceBadRequestException.class,
          () -> transactionService.withdraw(poorAccount.getNumber(), transactionDto),
          INSUFFICIENT_FUNDS_MSG
      );
   }


   @Test
   void transfer_whenConditionsOk_thenTransfersMoney() {
      when(accountService.find(richAccount.getNumber())).thenReturn(richAccount);
      when(accountService.find(poorAccount.getNumber())).thenReturn(poorAccount);
      when(encoder.matches(null, null)).thenReturn(true);
      when(transactionMapper.transactionToTransactionDto(null)).thenReturn(transactionDto);

      transactionService.transfer(richAccount.getNumber(), transactionDto);

      verify(accountService, times(2)).updateBalance(ArgumentMatchers.any(UUID.class), newBalanceCaptor.capture());
      verify(transactionRepository).save(transactionCaptor.capture());

      List<BigDecimal> balances = newBalanceCaptor.getAllValues();
      Transaction transaction = transactionCaptor.getValue();

      assertAll(
          () -> assertEquals(BigDecimal.valueOf(9), balances.get(0)),
          () -> assertEquals(BigDecimal.valueOf(1), balances.get(1)),
          () -> assertEquals(TransactionOperation.TRANSFER, transaction.getOperation()),
          () -> assertEquals(BigDecimal.ONE, transaction.getSum()),
          () -> assertNotNull(transaction.getTimestamp()),
          () -> assertEquals(richAccount.getNumber(), transaction.getAccount()),
          () -> assertEquals(poorAccount.getNumber(), transaction.getReceiver())
      );
   }

   @Test
   void transfer_whenPinIncorrect_thenThrowsException() {
      when(accountService.find(richAccount.getNumber())).thenReturn(richAccount);
      when(accountService.find(poorAccount.getNumber())).thenReturn(null);
      when(encoder.matches(null, null)).thenReturn(false);

      assertThrows(
          BankServiceBadRequestException.class,
          () -> transactionService.transfer(richAccount.getNumber(), transactionDto),
          INCORRECT_PIN_MSG
      );
   }

   @Test
   void transfer_whenSourceAndDestinationAreSame_thenThrowsException() {
      TransactionDto transactionDto = TransactionDto.builder().receiver(richAccount.getNumber()).build();

      when(accountService.find(richAccount.getNumber())).thenReturn(richAccount);
      when(encoder.matches(null, null)).thenReturn(true);

      assertThrows(
          BankServiceBadRequestException.class,
          () -> transactionService.transfer(richAccount.getNumber(), transactionDto),
          SRC_DST_ERR_MSG
      );
   }

   @Test
   void transfer_whenInsufficientFunds_thenThrowsException() {
      TransactionDto transactionDto = TransactionDto.builder().sum(BigDecimal.TEN).receiver(richAccount.getNumber()).build();

      when(accountService.find(poorAccount.getNumber())).thenReturn(poorAccount);
      when(accountService.find(richAccount.getNumber())).thenReturn(richAccount);
      when(encoder.matches(null, null)).thenReturn(true);

      assertThrows(
          BankServiceBadRequestException.class,
          () -> transactionService.transfer(poorAccount.getNumber(), transactionDto),
          INSUFFICIENT_FUNDS_MSG
      );
   }
}