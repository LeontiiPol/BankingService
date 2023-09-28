package com.example.bankingservice.mapper;

import com.example.bankingservice.dto.TransactionDto;
import com.example.bankingservice.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
   TransactionDto transactionToTransactionDto(Transaction transaction);

}
