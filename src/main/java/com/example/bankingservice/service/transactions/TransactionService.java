package com.example.bankingservice.service.transactions;

import com.example.bankingservice.dto.TransactionDto;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
   TransactionDto deposit(UUID accountNumber, TransactionDto transactionDto);
   TransactionDto withdraw(UUID accountNumber, TransactionDto transactionDto);
   TransactionDto transfer(UUID accountNumber, TransactionDto transactionDto);
   List<TransactionDto> getTransactions(UUID accountNumber);
}
