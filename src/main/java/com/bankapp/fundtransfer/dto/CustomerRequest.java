package com.bankapp.fundtransfer.dto;

public record CustomerRequest(String firstName, 
						  String lastName,
		                  String gender,
		                  String dateOfBirth, 
		                  String address,
		                  String state,
		                  String pincode,
						  String email,
						  String mobileNumber,
						  String password,
						  String typeOfAccount
             )
{

}
