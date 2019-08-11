package com.example.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.utility.entity.PrimaryTransaction;

@Repository
public interface PrimaryTransactionRepository extends JpaRepository<PrimaryTransaction, Long> {

	
}
