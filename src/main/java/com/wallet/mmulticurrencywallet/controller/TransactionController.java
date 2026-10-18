package com.wallet.mmulticurrencywallet.controller;

import com.wallet.mmulticurrencywallet.model.Transaction;
import com.wallet.mmulticurrencywallet.repository.TransactionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<Transaction>> getTransactionsByWallet(@PathVariable Long walletId) {
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("/wallet/{walletId}/export")
    public ResponseEntity<byte[]> exportTransactionsToCsv(@PathVariable Long walletId) {
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);

        StringBuilder csvContent = new StringBuilder();

        csvContent.append("ID;Тип;Сумма;Валюта;Описание;Дата\n");

        for (Transaction tx : transactions) {
            csvContent.append(tx.getId()).append(";")
                    .append(tx.getType()).append(";")
                    .append(tx.getAmount()).append(";")
                    .append(tx.getCurrency()).append(";")
                    .append(tx.getDescription() != null ? tx.getDescription() : "").append(";")
                    .append(tx.getTimestamp()).append("\n");
        }

        byte[] csvBytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statement_wallet_" + walletId + ".csv")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }
}