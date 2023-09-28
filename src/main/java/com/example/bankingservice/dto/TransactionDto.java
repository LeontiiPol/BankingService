package com.example.bankingservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class TransactionDto {
   @Schema(description = "Bank account number")
   private final UUID account;
   @JsonInclude(JsonInclude.Include.NON_NULL)
   @Schema(description = "Receiver bank account number")
   private final UUID receiver;
   @Positive(message = "Money amount must be specified")
   @Schema(description = "Transfer sum")
   private final BigDecimal sum;
   @Schema(description = "Operation timestamp")
   private final LocalDateTime timestamp;
   @Schema(description = "Type of transfer")
   private final TransactionOperation operation;
   @NotBlank(message = "PIN must be specified")
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   @JsonInclude(JsonInclude.Include.NON_NULL)
   @Schema(description = "Account PIN")
   private final String pin;
}
