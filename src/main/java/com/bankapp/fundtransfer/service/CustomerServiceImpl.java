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
import com.bankapp.fundtransfer.dto.CreditDebitRequest;
import com.bankapp.fundtransfer.dto.CustomerLoginResponse;
import com.bankapp.fundtransfer.dto.CustomerRequest;
import com.bankapp.fundtransfer.dto.Response;
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
						.email(customerRequest.email()).mobileNumber(customerRequest.mobileNumber()).bankName(CommonUtils.BANK_NAME).build();
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

			return BankResponse.builder().response(Response.builder().responseCode(CommonUtils.ACCOUNT_CREATED_CODE)
					.responseMessage(CommonUtils.ACCOUNT_CREATED_MESSAGE).build())
					.accountDetails(AccountDetailsResponse.builder()
							.accountHolderName(customer.getFirstName() + " " + customer.getLastName())
							.accountNumber(accountDetails.getAccountNumber())
							.accountBalance(accountDetails.getAccountBalance()).build())
					.build();
		} catch (Exception e) {
			return BankResponse.builder().response(Response.builder().responseCode(CommonUtils.SYSTEM_ERROR_CODE)
					.responseMessage(CommonUtils.SYSTEM_ERROR_MESSAGE + e).build()).build();
		}
	}

	public AccountDetails generateAccountDetails(CustomerRequest customerRequest) {
		return AccountDetails.builder()
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
							.map(acc -> AccountsDto.builder()
									.typeOfAccount(acc.getTypeOfAccount()).accountNumber(acc.getAccountNumber())
									.accountBalance(acc.getAccountBalance()).build())
							.collect(Collectors.toList());

					response = CustomerLoginResponse.builder().response(Response.builder().responseCode(CommonUtils.ACCOUNT_DETAILS_FETCHED_CODE)
							.responseMessage(CommonUtils.ACCOUNT_DETAILS_FETCHED_MESSAGE).build())
							.custName(customer.getFirstName() + " " + customer.getLastName()).accountsDetails(accounts)
							.build();
				}
				response = CustomerLoginResponse.builder().response(Response.builder().responseCode(CommonUtils.ACCOUNT_NOT_FOUND_CODE)
						.responseMessage(CommonUtils.ACCOUNT_NOT_FOUND_MESSAGE).build()).build();
			}
			response = CustomerLoginResponse.builder().response(Response.builder().responseCode(CommonUtils.CUSTOMER_NOT_FOUND_CODE)
					.responseMessage(CommonUtils.CUSTOMER_NOT_FOUND_MESSAGE).build()).build();
		} catch (Exception e) {
			return CustomerLoginResponse.builder().response(Response.builder().responseCode(CommonUtils.SYSTEM_ERROR_CODE)
					.responseMessage(CommonUtils.SYSTEM_ERROR_MESSAGE + e).build()).build();
		}
		return response;
	}

	@Override
	public BankResponse creditOrDebit(CreditDebitRequest creditDebitRequest) {
		try {
		if(accountDetailsRepository.existsByAccountNumber(creditDebitRequest.accountNumber())) {
			return BankResponse.builder()
					.response(Response.builder().responseCode(CommonUtils.ACCOUNT_NOT_FOUND_CODE)
							.responseMessage(CommonUtils.ACCOUNT_NOT_FOUND_MESSAGE)
							.build())
					.accountDetails(null)
					.build();
		}
		AccountDetails accountDetails = accountDetailsRepository.findByAccountNumber(creditDebitRequest.accountNumber());
		if (creditDebitRequest.creditOrDebit().equalsIgnoreCase(CommonUtils.CreditDebit.CREDIT.toString())) {
			accountDetails.setAccountBalance(accountDetails.getAccountBalance().add(creditDebitRequest.amount()));
			accountDetails = accountDetailsRepository.save(accountDetails);

			return BankResponse.builder()
					.response(Response.builder().responseCode(CommonUtils.CREDIT_SUCCESS_CODE)
							.responseMessage(CommonUtils.CREDIT_SUCCESS_MESSAGE).build())
					.accountDetails(AccountDetailsResponse.builder().accountBalance(accountDetails.getAccountBalance())
							.accountNumber(accountDetails.getAccountNumber()).build())
					.build();
		}else if(creditDebitRequest.creditOrDebit().equalsIgnoreCase(CommonUtils.CreditDebit.DEBIT.toString())) {
			if(accountDetails.getAccountBalance().compareTo(creditDebitRequest.amount()) < 0) {
				return BankResponse.builder()
						.response(Response.builder().responseCode(CommonUtils.INSUFFICIENT_BALANCE_CODE)
								.responseMessage(CommonUtils.INSUFFICIENT_BALANCE_MESSAGE)
								.build())
						.accountDetails(null)
						.build();
			}
			accountDetails.setAccountBalance(accountDetails.getAccountBalance().subtract(creditDebitRequest.amount()));
			accountDetails = accountDetailsRepository.save(accountDetails);
			
			return BankResponse.builder()
					.response(Response.builder().responseCode(CommonUtils.DEBIT_SUCCESS_CODE)
							.responseMessage(CommonUtils.DEBIT_SUCCESS_MESSAGE)
							.build())
					.accountDetails(AccountDetailsResponse.builder()
							.accountBalance(accountDetails.getAccountBalance())
							.accountNumber(accountDetails.getAccountNumber())
							.build())
					.build();
		}
		}catch (Exception e) {
			return BankResponse.builder().response(Response.builder().responseCode(CommonUtils.SYSTEM_ERROR_CODE)
					.responseMessage(CommonUtils.SYSTEM_ERROR_MESSAGE + e).build()).build();
		}
		return null;
	
	}

	@Override
	public BankResponse balanceEnquiry(String accountNo) {
		try {
			if(!accountDetailsRepository.existsByAccountNumber(accountNo)) {
				return BankResponse.builder()
						.response(Response.builder().responseCode(CommonUtils.ACCOUNT_NOT_FOUND_CODE)
								.responseMessage(CommonUtils.ACCOUNT_NOT_FOUND_MESSAGE)
								.build())
						.accountDetails(null)
						.build();
			}else {
			AccountDetails accountDetails = accountDetailsRepository.findByAccountNumber(accountNo);
			return BankResponse.builder()
					.response(Response.builder().responseCode(CommonUtils.ACCOUNT_DETAILS_FETCHED_CODE)
							.responseMessage(CommonUtils.ACCOUNT_DETAILS_FETCHED_MESSAGE)
							.build())
					.accountDetails(AccountDetailsResponse.builder()
							.accountBalance(accountDetails.getAccountBalance())
							.accountNumber(accountDetails.getAccountNumber())
							.build())
					.build();
			}
			
		}catch (Exception e) {
			return BankResponse.builder().response(Response.builder().responseCode(CommonUtils.SYSTEM_ERROR_CODE)
					.responseMessage(CommonUtils.SYSTEM_ERROR_MESSAGE + e).build()).build();
		}
		
	}
	
}
