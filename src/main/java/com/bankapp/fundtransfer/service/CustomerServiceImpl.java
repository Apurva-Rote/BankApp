package com.bankapp.fundtransfer.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankapp.fundtransfer.dto.AccountDetailsResponse;
import com.bankapp.fundtransfer.dto.AccountsDto;
import com.bankapp.fundtransfer.dto.BankResponse;
import com.bankapp.fundtransfer.dto.CustomerLoginResponse;
import com.bankapp.fundtransfer.dto.CustomerRequest;
import com.bankapp.fundtransfer.entity.AccountDetails;
import com.bankapp.fundtransfer.entity.Customer;
import com.bankapp.fundtransfer.entity.UserCredentials;
import com.bankapp.fundtransfer.repository.AccountDetailsRepository;
import com.bankapp.fundtransfer.repository.CustomerRepository;
import com.bankapp.fundtransfer.repository.UserCredentialRepository;
import com.bankapp.fundtransfer.utils.CommonUtils;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	AccountDetailsRepository accountDetailsRepository;
	@Autowired
	UserCredentialRepository userDetailsRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public BankResponse createCustomerAccount(CustomerRequest customerRequest) {
		try {
			Customer customer = null;
			AccountDetails accountDetails = null;
			// Check if customer already has account
			customer = customerRepository.getByMobileNumber(customerRequest.mobileNumber());
			if (customer == null) {
				// Creating customer account with new mobile number
				customer = Customer.builder().firstName(customerRequest.firstName())
						.lastName(customerRequest.lastName()).gender(customerRequest.gender())
						.dateOfBirth(CommonUtils.convertStringtoLocalDate(customerRequest.dateOfBirth()))
						.address(customerRequest.address()).state(customerRequest.state())
						.email(customerRequest.email()).mobileNumber(customerRequest.mobileNumber()).build();
				Customer savedCustomer = customerRepository.save(customer);
				accountDetails = generateAccountDetails(customerRequest);
				accountDetails.setCustomer(savedCustomer);
				accountDetailsRepository.saveAndFlush(accountDetails);
				UserCredentials userDetails = UserCredentials.builder().mobileNo(customerRequest.mobileNumber())
						.password(passwordEncoder.encode(customerRequest.password()))
						.customer(savedCustomer).build();
				userDetailsRepository.saveAndFlush(userDetails);
			} else {
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
		} catch (Exception e) {
			return BankResponse.builder().responseCode(CommonUtils.SYSTEM_ERROR_CODE)
					.responseMessage(CommonUtils.ACCOUNT_CREATED_MESSAGE + e).build();
		}
	}

	public AccountDetails generateAccountDetails(CustomerRequest customerRequest) {
		return AccountDetails.builder().bankName(customerRequest.bankName())
				.accountNumber(CommonUtils.generateAccountNumber()).accountBalance(BigDecimal.ZERO)
				.typeOfAccount(customerRequest.typeOfAccount()).build();

	}

	@Override
	public CustomerLoginResponse accountDetails(String mobileNumber) {
		CustomerLoginResponse response = null;
		try {
			Customer customer = customerRepository.getByMobileNumber(mobileNumber);
			if (customer != null) {
				List<AccountDetails> allAccounts = accountDetailsRepository.findByCustomer(customer);
				if (!allAccounts.isEmpty()) {
					List<AccountsDto> accounts = allAccounts.stream()
							.map(a -> AccountsDto.builder().bankName(a.getBankName())
									.typeOfAccount(a.getTypeOfAccount()).accountNumber(a.getAccountNumber())
									.accountBalance(a.getAccountBalance()).build())
							.collect(Collectors.toList());

					response = CustomerLoginResponse.builder().responseCode(CommonUtils.ACCOUNT_DETAILS_FETCHED_CODE)
							.responseMessage(CommonUtils.ACCOUNT_DETAILS_FETCHED_MESSAGE)
							.custName(customer.getFirstName() + " " + customer.getLastName()).accountsDetails(accounts)
							.build();
				}
				response = CustomerLoginResponse.builder().responseCode(CommonUtils.ACCOUNT_DETAILS_NOT_FOUND_CODE)
						.responseMessage(CommonUtils.ACCOUNT_DETAILS_NOT_FOUND_MESSAGE).build();
			}
			response = CustomerLoginResponse.builder().responseCode(CommonUtils.CUSTOMER_NOT_FOUND_CODE)
					.responseMessage(CommonUtils.CUSTOMER_NOT_FOUND_MESSAGE).build();
		} catch (Exception e) {
			return CustomerLoginResponse.builder().responseCode(CommonUtils.SYSTEM_ERROR_CODE)
					.responseMessage(CommonUtils.SYSTEM_ERROR_MESSAGE + e).build();
		}
		return response;
	}

}
