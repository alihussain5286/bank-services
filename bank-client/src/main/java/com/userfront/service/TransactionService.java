package com.userfront.service;

import java.util.List;

import com.example.utility.entity.PrimaryTransaction;
import com.example.utility.entity.SavingsTransaction;
import com.example.utility.exception.ApiException;

public interface TransactionService {
    
	List<PrimaryTransaction> findPrimaryTransactionList(String username) throws ApiException;

    List<SavingsTransaction> findSavingsTransactionList(String username) throws ApiException;
}
