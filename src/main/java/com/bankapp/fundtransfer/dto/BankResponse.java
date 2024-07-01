package com.bankapp.fundtransfer.dto;

import lombok.Builder;

@Builder
public record BankResponse(String responseCode,
		String responseMessage,
		AccountDetailsResponse accountDetails) {

}
