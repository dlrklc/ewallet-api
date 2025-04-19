package com.walletapp.service;

import com.walletapp.model.Transaction;
import com.walletapp.model.TransactionType;
import com.walletapp.model.Wallet;
import com.walletapp.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class TransactionService {

    @Autowired
    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    public void createBasic(BigDecimal amount, TransactionType transactionType) {

        Wallet wallet = walletService.updateBalance(Optional.empty(), amount, transactionType);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(wallet.getUser());
        transaction.setReceiver(wallet.getUser());
        transaction.setType(transactionType);

        transactionRepository.save(transaction);
    }

    public void createTransfer(Long receiverId, BigDecimal amount, TransactionType transactionType) {

        //withdraw from sender
        Wallet wallet = walletService.updateBalance(Optional.empty(), amount, TransactionType.WITHDRAW);

        //deposit to receiver
        Wallet receiverWallet = walletService.updateBalance(Optional.of(receiverId), amount, TransactionType.DEPOSIT);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(wallet.getUser());
        transaction.setReceiver(receiverWallet.getUser());
        transaction.setType(transactionType);

        transactionRepository.save(transaction);
    }

}
