package com.example.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.utility.entity.SavingsAccount;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {

    public SavingsAccount findByAccountNumber (int accountNumber);
    
    @Query("SELECT  max(savingsAccount.accountNumber) FROM SavingsAccount savingsAccount")
    public Integer getLastAccountNumber();
}
