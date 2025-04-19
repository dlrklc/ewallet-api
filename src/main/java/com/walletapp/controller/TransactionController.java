package com.walletapp.controller;

import com.walletapp.dto.BaseTransactionRequest;
import com.walletapp.dto.TransferRequest;
import com.walletapp.model.IdempotencyKey;
import com.walletapp.model.TransactionType;
import com.walletapp.model.User;
import com.walletapp.repository.IdempotencyKeyRepository;
import com.walletapp.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;


@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@Transactional
public class TransactionController {

    @Autowired
    private final TransactionService transactionService;
    private final IdempotencyKeyRepository idempotencyKeyRepository;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                          @RequestBody BaseTransactionRequest request) {

        Optional<IdempotencyKey> existingKey = idempotencyKeyRepository.findByKey(idempotencyKey);

        if (existingKey.isPresent()) {
            return ResponseEntity.status(403).body("Transfer already processed with this Idempotency Key");
        }

        BigDecimal amount = request.getAmount();

        transactionService.createBasic(amount, TransactionType.DEPOSIT);

        IdempotencyKey newKey = new IdempotencyKey();
        newKey.setKey(idempotencyKey);
        idempotencyKeyRepository.save(newKey);

        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                           @RequestBody BaseTransactionRequest request) {

        Optional<IdempotencyKey> existingKey = idempotencyKeyRepository.findByKey(idempotencyKey);

        if (existingKey.isPresent()) {
            return ResponseEntity.status(403).body("Transfer already processed with this Idempotency Key");
        }

        BigDecimal amount = request.getAmount();

        transactionService.createBasic(amount, TransactionType.WITHDRAW);

        IdempotencyKey newKey = new IdempotencyKey();
        newKey.setKey(idempotencyKey);
        idempotencyKeyRepository.save(newKey);

        return ResponseEntity.ok("Withdraw successful");

    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                           @RequestBody TransferRequest request) {

        Optional<IdempotencyKey> existingKey = idempotencyKeyRepository.findByKey(idempotencyKey);

        if (existingKey.isPresent()) {
            return ResponseEntity.status(403).body("Transfer already processed with this Idempotency Key");
        }

        Long receiverId = request.getReceiverId();
        BigDecimal amount = request.getAmount();

        transactionService.createTransfer(receiverId, amount, TransactionType.TRANSFER);

        IdempotencyKey newKey = new IdempotencyKey();
        newKey.setKey(idempotencyKey);
        idempotencyKeyRepository.save(newKey);

        return ResponseEntity.ok("Transfer successful");

    }
}




