package com.example.bankingservice.exception.handler;

import com.example.bankingservice.dto.ErrorResponseBody;
import com.example.bankingservice.dto.ErrorType;
import com.example.bankingservice.exception.BankServiceBadRequestException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ExceptionHandler(value = MethodArgumentNotValidException.class)
   public ErrorResponseBody validationHandler(MethodArgumentNotValidException ex) {
      return ErrorResponseBody.builder()
          .type(ErrorType.VALIDATION)
          .message(getValidationMessage(ex))
          .timestamp(LocalDateTime.now())
          .build();
   }

   private String getValidationMessage(MethodArgumentNotValidException e) {
      return e.getAllErrors()
          .stream()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .collect(Collectors.joining(" | "));
   }

   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ExceptionHandler(value = BankServiceBadRequestException.class)
   public ErrorResponseBody validationHandler(BankServiceBadRequestException ex) {
      return ErrorResponseBody.builder()
          .type(ErrorType.REQUEST)
          .message(ex.getMessage())
          .timestamp(LocalDateTime.now())
          .build();
   }

   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   @ExceptionHandler(value = RuntimeException.class)
   public ErrorResponseBody validationHandler(RuntimeException ex) {
      return ErrorResponseBody.builder()
          .type(ErrorType.SERVER)
          .message(ex.getMessage())
          .timestamp(LocalDateTime.now())
          .build();
   }
}
