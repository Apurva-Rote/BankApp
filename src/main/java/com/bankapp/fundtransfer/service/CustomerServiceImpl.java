package com.bankapp.fundtransfer.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapp.fundtransfer.dto.AccountDetailsResponse;
import com.bankapp.fundtransfer.dto.BankResponse;
import com.bankapp.fundtransfer.dto.CustomerRequest;
import com.bankapp.fundtransfer.entity.AccountDetails;
import com.bankapp.fundtransfer.entity.Customer;
import com.bankapp.fundtransfer.repository.AccountDetailsRepository;
import com.bankapp.fundtransfer.repository.CustomerRepository;
import com.bankapp.fundtransfer.utils.CommonUtils;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	AccountDetailsRepository accountDetailsRepository;
	
	@Override
	public BankResponse createCustomerAccount(CustomerRequest customerRequest) {
		Customer customer = null;
		AccountDetails accountDetails = null;
		// Check if customer already has account
	    customer = customerRepository.getByMobileNumber(customerRequest.mobileNumber());
		if (customer == null) {
	    // Creating customer account with new mobile number
			customer = Customer.builder()
				.firstName(customerRequest.firstName())
				.lastName(customerRequest.lastName())
				.gender(customerRequest.gender())
				.dateOfBirth(CommonUtils.convertStringtoLocalDate(customerRequest.dateOfBirth()))
				.address(customerRequest.address()).state(customerRequest.state())
				.email(customerRequest.email())
				.mobileNumber(customerRequest.mobileNumber())
				.build();
			Customer savedCustomer = customerRepository.save(customer);
			accountDetails = generateAccountDetails(customerRequest);
			accountDetails.setCustomer(savedCustomer);
			accountDetailsRepository.saveAndFlush(accountDetails);
			
		}else {
			accountDetails = generateAccountDetails(customerRequest);
			accountDetails.setCustomer(customer);
			accountDetailsRepository.saveAndFlush(accountDetails);
		}

		return BankResponse.builder().responseCode(CommonUtils.ACCOUNT_CREATED_CODE)
				.responseMessage(CommonUtils.ACCOUNT_CREATED_MESSAGE)
				.accountDetails(AccountDetailsResponse.builder()
						.accountHolderName(customer.getFirstName() + " " + customer.getLastName())
						.accountNumber(accountDetails.getAccountNumber())
						.accountBalance(accountDetails.getAccountBalance()).build())
				.build();

	}

	public AccountDetails generateAccountDetails(CustomerRequest customerRequest) {
		AccountDetails accountDetails = AccountDetails.builder().bankName(customerRequest.bankName())
				.accountNumber(CommonUtils.generateAccountNumber()).accountBalance(BigDecimal.ZERO)
				.typeOfAccount(customerRequest.typeOfAccount())
				.build();
		return accountDetails;

	}

}
