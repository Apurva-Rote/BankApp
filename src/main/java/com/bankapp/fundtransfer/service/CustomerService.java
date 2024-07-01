package com.bankapp.fundtransfer.service;

import com.bankapp.fundtransfer.dto.BankResponse;
import com.bankapp.fundtransfer.dto.CustomerLoginResponse;
import com.bankapp.fundtransfer.dto.CustomerRequest;


public interface CustomerService {
	
	BankResponse createCustomerAccount(CustomerRequest customerRequest);

	CustomerLoginResponse accountDetails(String mobileNumber);
	
}
