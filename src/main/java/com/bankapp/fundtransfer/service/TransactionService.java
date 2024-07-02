package com.bankapp.fundtransfer.service;

import com.bankapp.fundtransfer.dto.GenerateStatementResponse;
import com.bankapp.fundtransfer.entity.TransactionHistory;

public interface TransactionService {
	
    void saveTransactionHistory(TransactionHistory history);

	GenerateStatementResponse generateTransactionStatement(String accountNo);
	
}
