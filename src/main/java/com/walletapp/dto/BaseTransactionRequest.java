package com.walletapp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseTransactionRequest {
    private BigDecimal amount;
}

