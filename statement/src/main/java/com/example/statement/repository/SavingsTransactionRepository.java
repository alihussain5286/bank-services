package com.example.statement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.utility.entity.SavingsTransaction;

@Repository
public interface SavingsTransactionRepository extends JpaRepository<SavingsTransaction, Long>{

   
}
