package com.example.bank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bank.Entity.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    
}
