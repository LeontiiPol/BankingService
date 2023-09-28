package com.example.bankingservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "BENEFICIARIES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Beneficiary {
   @Id
   @GeneratedValue
   private UUID id;
   private String name;
}
