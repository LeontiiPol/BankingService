package com.example.bankingservice.dao;

import com.example.bankingservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
   List<Transaction> findAllByAccount(UUID account);
}
