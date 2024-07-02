package com.bankapp.fundtransfer.dto;

import java.math.BigDecimal;

public record CreditDebitRequest(String accountNumber, BigDecimal amount, String creditOrDebit) {

}
