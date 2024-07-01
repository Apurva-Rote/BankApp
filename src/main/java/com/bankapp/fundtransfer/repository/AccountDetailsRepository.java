package com.bankapp.fundtransfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.fundtransfer.entity.AccountDetails;
import com.bankapp.fundtransfer.entity.Customer;

public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long>{
	List<AccountDetails> findByCustomer(Customer customer);
}
