package com.example.bankingservice.dao;

import com.example.bankingservice.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, UUID> {
   Beneficiary findByName(String name);
}
