package com.bankapp.fundtransfer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapp.fundtransfer.dto.GenerateStatementResponse;
import com.bankapp.fundtransfer.dto.Response;
import com.bankapp.fundtransfer.dto.TransactionDto;
import com.bankapp.fundtransfer.dto.TransferRequest;
import com.bankapp.fundtransfer.entity.AccountDetails;
import com.bankapp.fundtransfer.entity.TransactionHistory;
import com.bankapp.fundtransfer.repository.AccountDetailsRepository;
import com.bankapp.fundtransfer.repository.TransactionsRepository;
import com.bankapp.fundtransfer.utils.CommonUtils;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	AccountDetailsRepository accountDetailsRepository;
	@Autowired
	TransactionsRepository transactionsRepository;

	@Override
	public GenerateStatementResponse generateTransactionStatement(String accountNo) {
		List<TransactionHistory> transactionLogs = null;
		List<TransactionDto> history = null;
		AccountDetails accountDetails = accountDetailsRepository.findByAccountNumber(accountNo);
		if (accountDetails != null) {
			transactionLogs = transactionsRepository.findByAccountNo(accountDetails);
			history = transactionLogs.stream().map(a -> TransactionDto.builder()
					.accountDetails(CommonUtils.generateTransactionAccountDetails(accountDetails))
					.bankName(a.getBankName())
					.credit(a.getCredit()!=null?a.getCredit():null)
					.debit(a.getDebit()!= null? a.getDebit():null)
					.transactionDateTime(a.getTransactionDateTime())
					.closingBalance(a.getClosingBalance())
					.build()).collect(Collectors.toList());
		}else {
			return GenerateStatementResponse.builder()
					.response(Response.builder().responseCode(CommonUtils.ACCOUNT_NOT_FOUND_CODE)
					.responseMessage(CommonUtils.ACCOUNT_NOT_FOUND_MESSAGE).build())
					.build();
		}
		return GenerateStatementResponse.builder()
				.response(Response.builder().responseCode(CommonUtils.STATEMENT_GENERATED_CODE)
						.responseMessage(CommonUtils.STATEMENT_GENERATED_MESSAGE).build())
				.transactionHistory(history)
				.build();
	}

	@Override
	public void saveTransactionHistory(TransactionHistory history) {
		// TODO Auto-generated method stub
		
	}


}
