package com.wallet.mmulticurrencywallet.controller;

import com.wallet.mmulticurrencywallet.model.Wallet;
import com.wallet.mmulticurrencywallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@CrossOrigin(origins = "*")
public class WalletController {

    private final WalletService walletService;


    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestParam Long userId, @RequestParam String currency) {
        Wallet newWallet = walletService.createWallet(userId, currency);
        return ResponseEntity.ok(newWallet);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wallet>> getWalletsByUser(@PathVariable Long userId) {
        List<Wallet> wallets = walletService.getWalletsByUserId(userId);
        return ResponseEntity.ok(wallets);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Wallet> deposit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        Wallet updatedWallet = walletService.deposit(id, amount);
        return ResponseEntity.ok(updatedWallet);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @RequestParam Long fromWalletId,
            @RequestParam Long toWalletId,
            @RequestParam BigDecimal amount) {
        try {
            walletService.transfer(fromWalletId, toWalletId, amount);
            return ResponseEntity.ok("Перевод успешно выполнен!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convertCurrency(
            @RequestParam Long fromWalletId,
            @RequestParam Long toWalletId,
            @RequestParam BigDecimal amount) {
        try {
            walletService.convertCurrency(fromWalletId, toWalletId, amount);
            return ResponseEntity.ok("Конвертация успешно выполнена!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}