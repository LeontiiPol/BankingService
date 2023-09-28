package com.example.bankingservice.service.transactions;

import com.example.bankingservice.dao.TransactionRepository;
import com.example.bankingservice.dto.AccountDto;
import com.example.bankingservice.dto.TransactionDto;
import com.example.bankingservice.dto.TransactionOperation;
import com.example.bankingservice.exception.BankServiceBadRequestException;
import com.example.bankingservice.mapper.TransactionMapper;
import com.example.bankingservice.model.Transaction;
import com.example.bankingservice.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultTransactionService implements TransactionService {

   public static final String INCORRECT_PIN_MSG = "PIN doesn't match";
   public static final String SRC_DST_ERR_MSG = "Source and Destination accounts are the same";
   public static final String INSUFFICIENT_FUNDS_MSG = "Insufficient funds";

   private final AccountService accountService;
   private final TransactionRepository transactionRepository;
   private final PasswordEncoder encoder;
   private final TransactionMapper transactionMapper;

   @Override
   @Transactional
   public TransactionDto deposit(UUID accountNumber, TransactionDto transactionDto) {
      AccountDto account = accountService.find(accountNumber);

      if(isInvalidPin(transactionDto.getPin(), account.getPin())) {
         throw new BankServiceBadRequestException(INCORRECT_PIN_MSG);
      }
      return transactionMapper.transactionToTransactionDto(updateBalance(account, account.getBalance().add(transactionDto.getSum()), TransactionOperation.DEPOSIT));
   }

   private boolean isInvalidPin(String rawPin, String encodedPin) {
      return !encoder.matches(rawPin, encodedPin);
   }

   @Override
   @Transactional
   public TransactionDto withdraw(UUID accountNumber, TransactionDto transactionDto) {
      AccountDto account = accountService.find(accountNumber);

      if(isInvalidPin(transactionDto.getPin(), account.getPin())) {
         throw new BankServiceBadRequestException(INCORRECT_PIN_MSG);
      }

      if (isInsufficientFund(account.getBalance(), transactionDto.getSum())) {
         throw new BankServiceBadRequestException(INSUFFICIENT_FUNDS_MSG);
      }

      return transactionMapper.transactionToTransactionDto(updateBalance(account, account.getBalance().subtract(transactionDto.getSum()), TransactionOperation.WITHDRAW));
   }

   private boolean isInsufficientFund(BigDecimal accountBalance, BigDecimal withdrawSum) {
      return accountBalance.subtract(withdrawSum).compareTo(BigDecimal.ZERO) < 0;
   }

   @Override
   @Transactional
   public TransactionDto transfer(UUID accountNumber, TransactionDto transactionDto) {
      AccountDto account = accountService.find(accountNumber);
      AccountDto receiver = accountService.find(transactionDto.getReceiver());

      if(isInvalidPin(transactionDto.getPin(), account.getPin())) {
         throw new BankServiceBadRequestException(INCORRECT_PIN_MSG);
      }

      if (accountNumber.equals(receiver.getNumber())) {
         throw new BankServiceBadRequestException(SRC_DST_ERR_MSG);
      }

      if (isInsufficientFund(account.getBalance(), transactionDto.getSum())) {
         throw new BankServiceBadRequestException(INSUFFICIENT_FUNDS_MSG);
      }

      return transactionMapper.transactionToTransactionDto(transfer(account, receiver, transactionDto.getSum()));
   }

   @Override
   public List<TransactionDto> getTransactions(UUID accountNumber) {
      accountService.find(accountNumber);
      return transactionRepository.findAllByAccount(accountNumber)
          .stream()
          .map(transactionMapper::transactionToTransactionDto)
          .toList();
   }

   private Transaction updateBalance(AccountDto account, BigDecimal newBalance, TransactionOperation operation) {
      accountService.updateBalance(account.getNumber(), newBalance);
      return transactionRepository.save(
          new Transaction(
              account.getNumber(),
              null,
              operation,
              account.getBalance().subtract(newBalance).abs(),
              LocalDateTime.now()
          )
      );
   }

   private Transaction transfer(AccountDto account, AccountDto receiver, BigDecimal sum) {
      accountService.updateBalance(account.getNumber(), account.getBalance().subtract(sum));
      accountService.updateBalance(receiver.getNumber(), receiver.getBalance().add(sum));
      return transactionRepository.save(
          new Transaction(
              account.getNumber(),
              receiver.getNumber(),
              TransactionOperation.TRANSFER,
              sum,
              LocalDateTime.now()
          )
      );
   }
}
