package com.example.bankingservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class AccountDto {
   @Schema(description = "Bank account number")
   private final UUID number;
   @NotBlank(message = "Beneficiary name cannot be empty")
   @Schema(description = "Name of account holder")
   private final String beneficiaryName;
   @Pattern(regexp = "[\\d]{4}", message = "Pin must be 4-digit")
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   @JsonInclude(JsonInclude.Include.NON_NULL)
   @Schema(description = "Account PIN")
   private final String pin;
   @Schema(description = "Account balance")
   private final BigDecimal balance;
}
