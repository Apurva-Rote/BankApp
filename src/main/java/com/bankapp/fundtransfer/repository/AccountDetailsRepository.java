package com.bankapp.fundtransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.fundtransfer.entity.AccountDetails;

public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long>{
}
