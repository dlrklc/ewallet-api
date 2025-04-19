package com.walletapp.controller;

import com.walletapp.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@Transactional
public class WalletController {

    @Autowired
    private final WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance() {

        return ResponseEntity.ok(walletService.getBalance());
    }

}
