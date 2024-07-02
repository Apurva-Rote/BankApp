package com.bankapp.fundtransfer.dto;

import java.math.BigDecimal;

public record TransferRequest(String sourceAccountNumber, String destinationAccountnumber, BigDecimal amount) {

}
