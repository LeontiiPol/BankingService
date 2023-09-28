package com.example.bankingservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNTS")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
   @Id
   @GeneratedValue
   private UUID number;
   @ManyToOne
   @JoinColumn(name = "beneficiary")
   private Beneficiary beneficiary;
   private String pin;
   private BigDecimal balance;
}
