package com.walletapp.repository;

import com.walletapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Methods can be added for transaction history, transactions of a specific user, etc.
}
