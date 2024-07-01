package com.bankapp.fundtransfer.utils;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

public class CommonUtils {
	
	public static final String ACCOUNT_CREATED_CODE="001";
	public static final String ACCOUNT_CREATED_MESSAGE="Account created successfully.";
	
	//Convert the String date into Local Date
	public static LocalDate convertStringtoLocalDate(String date) {
		return LocalDate.parse(date);
	}

	//Generate current year + 6 digit account number
	public static String generateAccountNumber() {
		
		var currentYear = Year.now();
		
		var min = 100000;
		var max = 999999;
		
		var random =  (int) Math.floor(Math.random()*(max-min+1)+ min);
		
		var year = String.valueOf(currentYear);
		var randomNumber = String.valueOf(random);
		var accountNumber = new StringBuilder();
		
		return accountNumber.append(year).append(randomNumber).toString();
	}
	
	

}
