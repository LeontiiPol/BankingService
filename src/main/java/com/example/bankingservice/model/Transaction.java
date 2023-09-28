package com.example.bankingservice.model;

import com.example.bankingservice.dto.TransactionOperation;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TRANSACTIONS")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

   @Id
   @GeneratedValue
   private UUID id;
   private UUID account;
   private UUID receiver;
   @Enumerated(EnumType.STRING)
   private TransactionOperation operation;
   @Column(name = "transaction_sum")
   private BigDecimal sum;
   @Column(name = "transaction_timestamp")
   private LocalDateTime timestamp;

   public Transaction(UUID account, UUID receiver, TransactionOperation operation, BigDecimal sum, LocalDateTime timestamp) {
      this.account = account;
      this.receiver = receiver;
      this.operation = operation;
      this.sum = sum;
      this.timestamp = timestamp;
   }
}
