package com.walletapp.service;

import com.walletapp.model.TransactionType;
import com.walletapp.model.User;
import com.walletapp.model.Wallet;
import com.walletapp.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WalletService {

    @Autowired
    private final WalletRepository walletRepository;
    private final UserService userService;

    public Wallet getWalletByUserId(Long userId) {
        Optional<Wallet> wallet = walletRepository.findByUserId(userId);
        if (wallet.isPresent()) {
            return wallet.get();
        } else {
            throw new RuntimeException("Wallet not found for user id " + userId);
        }
    }

    public BigDecimal getBalance(Long userId) {
        return getWalletByUserId(userId).getBalance();
    }

    public Wallet updateBalance(Optional<Long> receiver, User sender, BigDecimal amount, TransactionType transactionType) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount must be greater than zero.");
        }

        Wallet wallet;

        if(receiver.isPresent()) {
            wallet = getWalletByUserId(receiver.get());
        }else{
            wallet = getWalletByUserId(sender.getId());
        }

        if (transactionType == TransactionType.WITHDRAW && wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        if (transactionType == TransactionType.WITHDRAW) {
            wallet.setBalance(wallet.getBalance().subtract(amount));
        } else {
            wallet.setBalance(wallet.getBalance().add(amount));
        }

        return walletRepository.save(wallet);
    }

}

