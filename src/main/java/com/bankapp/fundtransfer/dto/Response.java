package com.bankapp.fundtransfer.dto;

import lombok.Builder;

@Builder
public record Response(String responseCode, String responseMessage) {

}
