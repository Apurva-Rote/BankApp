package com.bankapp.fundtransfer.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record AccountsDto(String bankName, String accountNumber, BigDecimal accountBalance, String typeOfAccount) {

}
