package com.bankapp.fundtransfer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record TransactionDto(String accountDetails, String bankName,
		BigDecimal credit, BigDecimal debit, BigDecimal closingBalance, LocalDateTime transactionDateTime) {

}
