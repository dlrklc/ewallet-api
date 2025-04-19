package com.walletapp.controller;

import com.walletapp.dto.BaseTransactionRequest;
import com.walletapp.dto.TransactionSummary;
import com.walletapp.dto.TransferRequest;
import com.walletapp.model.IdempotencyKey;
import com.walletapp.model.Transaction;
import com.walletapp.model.TransactionType;
import com.walletapp.model.User;
import com.walletapp.repository.IdempotencyKeyRepository;
import com.walletapp.service.TransactionService;
import com.walletapp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@Transactional
public class TransactionController {

    @Autowired
    private final TransactionService transactionService;
    private final UserService userService;
    private final IdempotencyKeyRepository idempotencyKeyRepository;

    @GetMapping("/")
    public ResponseEntity<List<TransactionSummary>> getTransactions() {

        User user = userService.getAuthenticatedUser();
        return ResponseEntity.ok(transactionService.getTransactions(user));
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                          @RequestBody BaseTransactionRequest request) {

        Optional<IdempotencyKey> existingKey = idempotencyKeyRepository.findByKey(idempotencyKey);

        if (existingKey.isPresent()) {
            return ResponseEntity.status(403).body("Transfer already processed with this Idempotency Key");
        }

        User user = userService.getAuthenticatedUser();

        BigDecimal amount = request.getAmount();

        transactionService.createBasic(user, amount, TransactionType.DEPOSIT);

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

        User user = userService.getAuthenticatedUser();

        BigDecimal amount = request.getAmount();

        transactionService.createBasic(user, amount, TransactionType.WITHDRAW);

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

        User user = userService.getAuthenticatedUser();

        Long receiverId = request.getReceiverId();
        BigDecimal amount = request.getAmount();

        transactionService.createTransfer(receiverId, user, amount, TransactionType.TRANSFER);

        IdempotencyKey newKey = new IdempotencyKey();
        newKey.setKey(idempotencyKey);
        idempotencyKeyRepository.save(newKey);

        return ResponseEntity.ok("Transfer successful");

    }
}




