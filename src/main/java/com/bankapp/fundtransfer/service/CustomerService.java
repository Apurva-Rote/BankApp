package com.bankapp.fundtransfer.service;

import org.springframework.stereotype.Service;

import com.bankapp.fundtransfer.dto.BankResponse;
import com.bankapp.fundtransfer.dto.CustomerRequest;


public interface CustomerService {
	
	BankResponse createCustomerAccount(CustomerRequest customerRequest);

}
