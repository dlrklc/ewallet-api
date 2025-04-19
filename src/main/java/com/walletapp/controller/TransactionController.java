package com.walletapp.controller;

import com.walletapp.dto.BaseTransactionRequest;
import com.walletapp.dto.TransferRequest;
import com.walletapp.model.TransactionType;
import com.walletapp.model.User;
import com.walletapp.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@Transactional
public class TransactionController {

    @Autowired
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody BaseTransactionRequest request) {

        BigDecimal amount = request.getAmount();

        transactionService.createBasic(amount, TransactionType.DEPOSIT);
        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody BaseTransactionRequest request) {

        BigDecimal amount = request.getAmount();

        transactionService.createBasic(amount, TransactionType.WITHDRAW);
        return ResponseEntity.ok("Withdraw successful");

    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {

        Long receiverId = request.getReceiverId();
        BigDecimal amount = request.getAmount();

        transactionService.createTransfer(receiverId, amount, TransactionType.TRANSFER);
        return ResponseEntity.ok("Transfer successful");

    }
}




