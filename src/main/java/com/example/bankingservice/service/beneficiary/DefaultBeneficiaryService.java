package com.example.bankingservice.service.beneficiary;

import com.example.bankingservice.dao.BeneficiaryRepository;
import com.example.bankingservice.exception.BankServiceBadRequestException;
import com.example.bankingservice.model.Beneficiary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultBeneficiaryService implements BeneficiaryService {

   public static final String BENEFICIARY_NOT_FND_MSG = "Beneficiary %s not found. Check beneficiary name";

   private final BeneficiaryRepository repository;

   @Override
   public Beneficiary findByName(String name) {
      return Optional.ofNullable(repository.findByName(name))
          .orElseThrow(() -> new BankServiceBadRequestException(String.format(BENEFICIARY_NOT_FND_MSG, name)));
   }
}
