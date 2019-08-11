package com.example.statement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.utility.entity.PrimaryAccount;

@Repository
public interface PrimaryAccountRepository extends JpaRepository<PrimaryAccount, Long>{

   
}
