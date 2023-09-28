package com.example.bankingservice.controller;

import com.example.bankingservice.dto.AccountDto;
import com.example.bankingservice.dto.TransactionDto;
import com.example.bankingservice.service.account.AccountService;
import com.example.bankingservice.service.transactions.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

   private final AccountService accountService;
   private final TransactionService transactionService;

   @PostMapping
   @Operation(summary = "Creating an account", description = "Creates new bank account for specified beneficiary name")
   public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid AccountDto accountDto) {
      return ResponseEntity.ok(accountService.createAccount(accountDto));
   }

   @PostMapping("/{number}/deposit")
   @Operation(summary = "Deposit", description = "Increase account balance")
   public ResponseEntity<TransactionDto> deposit(@Parameter(description = "Account number") @PathVariable UUID number,
                                                 @RequestBody @Valid TransactionDto transactionDto) {
      return ResponseEntity.ok(transactionService.deposit(number, transactionDto));
   }

   @PostMapping("/{number}/withdraw")
   @Operation(summary = "Withdraw", description = "Decrease account balance")
   public ResponseEntity<TransactionDto> withdraw(@PathVariable UUID number,
                                                  @RequestBody @Valid TransactionDto transactionDto) {
      return ResponseEntity.ok(transactionService.withdraw(number, transactionDto));
   }

   @PostMapping("/{number}/transfer")
   @Operation(summary = "Transfer", description = "Transfers money from source balance to target balance")
   public ResponseEntity<TransactionDto> transfer(@PathVariable UUID number,
                                                  @RequestBody @Valid TransactionDto transactionDto) {
      return ResponseEntity.ok(transactionService.transfer(number, transactionDto));
   }

   @GetMapping
   @Operation(description = "Returns all bank accounts information")
   public ResponseEntity<List<AccountDto>> getAccounts() {
      return ResponseEntity.ok(accountService.findAll());
   }

   @GetMapping("/{number}/transactions")
   @Operation(description = "Returns all transaction information of specified bank account")
   public ResponseEntity<List<TransactionDto>> getTransactions(@PathVariable UUID number) {
      return ResponseEntity.ok(transactionService.getTransactions(number));
   }
}
