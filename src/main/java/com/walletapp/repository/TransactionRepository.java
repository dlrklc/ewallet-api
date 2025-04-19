package com.walletapp.repository;

import com.walletapp.dto.TransactionSummary;
import com.walletapp.model.Transaction;
import com.walletapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
    SELECT new com.walletapp.dto.TransactionSummary(
        t.amount, t.sender.id, t.receiver.id, t.type, t.createdAt, t.status
    )
    FROM Transaction t
    WHERE t.sender = :sender
    """)
    List<TransactionSummary> findAllBySenderAsSummary(User sender);
}
