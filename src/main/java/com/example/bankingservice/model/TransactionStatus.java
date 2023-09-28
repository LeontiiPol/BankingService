package com.example.bankingservice.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatus {

   @Id
   @GeneratedValue
   private UUID id;
   private String name;
}