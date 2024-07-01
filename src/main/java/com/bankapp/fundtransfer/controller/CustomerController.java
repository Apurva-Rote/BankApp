package com.bankapp.fundtransfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.fundtransfer.dto.BankResponse;
import com.bankapp.fundtransfer.dto.CustomerLoginResponse;
import com.bankapp.fundtransfer.dto.CustomerRequest;
import com.bankapp.fundtransfer.service.CustomerService;

@RestController
public class CustomerController {
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/createCustomer")
	public ResponseEntity<BankResponse> createCustomer(@RequestBody CustomerRequest customerRequest){
		BankResponse bankResponse = customerService.createCustomerAccount(customerRequest);
		return new ResponseEntity(bankResponse, HttpStatus.CREATED);
	}
	
	@GetMapping("/customerLogin/{mobileNumber}")
	public ResponseEntity<BankResponse> customerLogin(@PathVariable String mobileNumber){
		CustomerLoginResponse response = customerService.accountDetails(mobileNumber);
		return new ResponseEntity(response, HttpStatus.FOUND);
	}
	
}
