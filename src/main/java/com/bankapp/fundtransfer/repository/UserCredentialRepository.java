package com.bankapp.fundtransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.fundtransfer.entity.UserCredentials;

public interface UserCredentialRepository extends JpaRepository<UserCredentials, Long>{
	
	UserCredentials getByMobileNo(String mobileNo);
      
}
