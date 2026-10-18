package com.wallet.mmulticurrencywallet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long walletId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private String description;

    public Transaction() {}

    public Transaction(Long walletId, String type, BigDecimal amount, String currency, String description) {
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }


    public Long getId() { return id; }
    public Long getWalletId() { return walletId; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    public void setId(Long id) { this.id = id; }
    public void setWalletId(Long walletId) { this.walletId = walletId; }
    public void setType(String type) { this.type = type; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setDescription(String description) { this.description = description; }
}