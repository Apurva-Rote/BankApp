package com.bankapp.fundtransfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.fundtransfer.entity.AccountDetails;
import com.bankapp.fundtransfer.entity.TransactionHistory;


public interface TransactionsRepository extends JpaRepository<TransactionHistory,Long>{
	
	List<TransactionHistory> findByAccountNo(AccountDetails accountNo);
}
