package com.example.bankingservice.exception;

public class BankServiceBadRequestException extends RuntimeException {

   public BankServiceBadRequestException(String message) {
      super(message);
   }
}
