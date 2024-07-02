package com.bankapp.fundtransfer.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TransactionHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String transferAccountDetails;
	private String bankName;
	private BigDecimal credit;
	private BigDecimal debit;
	@CreationTimestamp
	private LocalDateTime transactionDateTime;
	private BigDecimal closingBalance;
	private String transactionStatus;
	@ManyToOne
	@JoinColumn(name = "account_id")
	private AccountDetails accountNo;
	
}
