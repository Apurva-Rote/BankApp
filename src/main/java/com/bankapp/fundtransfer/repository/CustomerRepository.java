package com.bankapp.fundtransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.fundtransfer.dto.CustomerRequest;
import com.bankapp.fundtransfer.entity.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long>  {
 Customer getByMobileNumber(String mobileNumber);
}
