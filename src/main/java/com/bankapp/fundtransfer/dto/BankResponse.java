package com.bankapp.fundtransfer.dto;

import lombok.Builder;

@Builder
public record BankResponse(Response response,
		AccountDetailsResponse accountDetails) {

}
