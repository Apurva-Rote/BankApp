package com.bankapp.fundtransfer.dto;

import java.util.List;

import lombok.Builder;
@Builder
public record GenerateStatementResponse(Response response, List<TransactionDto> transactionHistory) {

}
