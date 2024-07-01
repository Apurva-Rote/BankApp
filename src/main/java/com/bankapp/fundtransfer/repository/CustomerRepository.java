package com.bankapp.fundtransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.fundtransfer.entity.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long>  {
 Customer getByMobileNumber(String mobileNumber);
}
