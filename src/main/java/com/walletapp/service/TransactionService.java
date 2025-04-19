package com.walletapp.service;

import com.walletapp.dto.TransactionSummary;
import com.walletapp.model.Transaction;
import com.walletapp.model.TransactionType;
import com.walletapp.model.User;
import com.walletapp.model.Wallet;
import com.walletapp.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class TransactionService {

    @Autowired
    private final WalletService walletService;
    private final UserService userService;
    private final TransactionRepository transactionRepository;

    public List<TransactionSummary> getTransactions(User user) {
        List<TransactionSummary> transactions = transactionRepository.findAllBySenderAsSummary(user);
        if (transactions.isEmpty()) {
            throw new RuntimeException("Transactions not found for user id " + user.getId());
        } else {
            return transactions;
        }
    }

    public void createBasic(User user, BigDecimal amount, TransactionType transactionType) {

        walletService.updateBalance(Optional.empty(), user, amount, transactionType);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(user);
        transaction.setReceiver(user);
        transaction.setType(transactionType);

        transactionRepository.save(transaction);
    }

    public void createTransfer(Long receiverId, User sender, BigDecimal amount, TransactionType transactionType) {

        //withdraw from sender
        Wallet wallet = walletService.updateBalance(Optional.empty(), sender, amount, TransactionType.WITHDRAW);

        //deposit to receiver
        Wallet receiverWallet = walletService.updateBalance(Optional.of(receiverId), sender, amount, TransactionType.DEPOSIT);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(sender);
        transaction.setReceiver(receiverWallet.getUser());
        transaction.setType(transactionType);

        transactionRepository.save(transaction);
    }

}
