package com.bankapp.fundtransfer.utils;

import java.time.LocalDate;
import java.time.Year;
import java.util.Random;

public class CommonUtils {

	public static final String ACCOUNT_CREATED_CODE = "001";
	public static final String ACCOUNT_CREATED_MESSAGE = "Account created successfully.";

	public static final String ACCOUNT_CREATION_FAILED_CODE = "002";
	public static final String ACCOUNT_CREATION_FAILED_MESSAGE = "Account creation failed.";

	public static final String ACCOUNT_DETAILS_FETCHED_CODE = "003";
	public static final String ACCOUNT_DETAILS_FETCHED_MESSAGE = "Account details fetched succesfully";

	public static final String ACCOUNT_DETAILS_NOT_FOUND_CODE = "004";
	public static final String ACCOUNT_DETAILS_NOT_FOUND_MESSAGE = "Account details not found";

	public static final String CUSTOMER_NOT_FOUND_CODE = "005";
	public static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer not found";

	public static final String SYSTEM_ERROR_CODE = "006";
	public static final String SYSTEM_ERROR_MESSAGE = "Opps! System error";

	// Convert the String date into Local Date
	public static LocalDate convertStringtoLocalDate(String date) {
		return LocalDate.parse(date);
	}

	// Generate current year + 6 digit account number
	public static String generateAccountNumber() {

		var currentYear = Year.now();

		var min = 100000;
		var max = 999999;

		var random = (int) Math.floor(Math.random() * (max - min + 1) + min);

		var year = String.valueOf(currentYear);
		var randomNumber = String.valueOf(random);
		var accountNumber = new StringBuilder();

		return accountNumber.append(year).append(randomNumber).toString();
	}

}
