package com.walletapp.dto;

import com.walletapp.model.TransactionStatus;
import com.walletapp.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionSummary {
    private BigDecimal amount;
    private Long senderId;
    private Long receiverId;
    private TransactionType type;
    private LocalDateTime createdAt;
    private TransactionStatus status;

    public TransactionSummary(BigDecimal amount, Long senderId, Long receiverId, TransactionType type,
                          LocalDateTime createdAt, TransactionStatus status) {
        this.amount = amount;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
        this.createdAt = createdAt;
        this.status = status;
    }
}
