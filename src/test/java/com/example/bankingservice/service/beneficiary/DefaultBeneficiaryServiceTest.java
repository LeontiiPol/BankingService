package com.example.bankingservice.service.beneficiary;

import com.example.bankingservice.dao.BeneficiaryRepository;
import com.example.bankingservice.exception.BankServiceBadRequestException;
import com.example.bankingservice.model.Beneficiary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;

import static com.example.bankingservice.service.beneficiary.DefaultBeneficiaryService.BENEFICIARY_NOT_FND_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultBeneficiaryServiceTest {

   @Mock
   private BeneficiaryRepository repository;
   @InjectMocks
   private DefaultBeneficiaryService beneficiaryService;

   @Test
   void findByName_whenBeneficiaryExists_thenModelReturned() {
      Beneficiary beneficiary = new Beneficiary(UUID.randomUUID(), "Nick");

      when(repository.findByName("Nick")).thenReturn(beneficiary);

      assertEquals(beneficiary.getName(), beneficiaryService.findByName("Nick").getName());
   }

   @Test
   void findByName_whenBeneficiaryNotExists_thenThrowsException() {
      assertThrows(
          BankServiceBadRequestException.class,
          () -> beneficiaryService.findByName("Nick"),
          String.format(BENEFICIARY_NOT_FND_MSG, "Nick")
      );
   }
}