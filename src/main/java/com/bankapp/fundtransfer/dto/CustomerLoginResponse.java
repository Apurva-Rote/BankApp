package com.bankapp.fundtransfer.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record CustomerLoginResponse(Response response, String custName,
		List<AccountsDto> accountsDetails) {

}
