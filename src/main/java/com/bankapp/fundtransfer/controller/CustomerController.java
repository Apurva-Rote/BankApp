package com.bankapp.fundtransfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.fundtransfer.dto.BankResponse;
import com.bankapp.fundtransfer.dto.CustomerRequest;
import com.bankapp.fundtransfer.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	CustomerService userService;
	
	@PostMapping("/createCustomer")
	public ResponseEntity<BankResponse> createCustomer(@RequestBody CustomerRequest customerRequest){
		BankResponse bankResponse = userService.createCustomerAccount(customerRequest);
		return new ResponseEntity(bankResponse, HttpStatus.CREATED);
	}

}
