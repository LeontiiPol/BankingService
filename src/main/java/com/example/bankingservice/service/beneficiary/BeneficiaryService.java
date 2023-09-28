package com.example.bankingservice.service.beneficiary;

import com.example.bankingservice.model.Beneficiary;

public interface BeneficiaryService {

   Beneficiary findByName(String name);
}
