package com.example.payment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.utility.entity.PrimaryAccount;

@Repository
public interface PrimaryAccountRepository extends JpaRepository<PrimaryAccount, Long>{

	public PrimaryAccount findByAccountNumber (int accountNumber);

	@Query("SELECT  max(primaryAccount.accountNumber) FROM PrimaryAccount primaryAccount")
	public  Integer getLastAccountNumber();

}
