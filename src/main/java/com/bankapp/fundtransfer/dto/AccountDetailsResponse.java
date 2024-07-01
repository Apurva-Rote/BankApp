package com.bankapp.fundtransfer.dto;

import java.math.BigDecimal;

import lombok.Builder;
@Builder
public record AccountDetailsResponse(String accountHolderName, String accountNumber, BigDecimal accountBalance) {
      
}
