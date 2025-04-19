package com.walletapp.controller;

import com.walletapp.model.User;
import com.walletapp.service.UserService;
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
    private final UserService userService;

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance() {
        User user = userService.getAuthenticatedUser();
        return ResponseEntity.ok(walletService.getBalance(user.getId()));
    }

}
