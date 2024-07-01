package com.bankapp.fundtransfer.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record CustomerLoginResponse(String responseCode, String responseMessage, String custName,
		List<AccountsDto> accountsDetails) {

}
