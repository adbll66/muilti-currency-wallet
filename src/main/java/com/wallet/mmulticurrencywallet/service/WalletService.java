package com.wallet.mmulticurrencywallet.service;

import com.wallet.mmulticurrencywallet.model.Users;
import com.wallet.mmulticurrencywallet.model.Wallet;
import com.wallet.mmulticurrencywallet.repository.UserRepository;
import com.wallet.mmulticurrencywallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    public Wallet createWallet(Long userId, String currency) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Wallet wallet = new Wallet(user, currency);
        return walletRepository.save(wallet);
    }

    public List<Wallet> getWalletsByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    public Optional<Wallet> getWalletById(Long id) {
        return walletRepository.findById(id);
    }

    public Wallet deposit(Long walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть больше нуля");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Кошелек с ID " + walletId + " не найден"));

        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.save(wallet);
    }

    @Transactional
    public void transfer(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше нуля");
        }

        Wallet fromWallet = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new RuntimeException("Кошелек отправителя с ID " + fromWalletId + " не найден"));

        Wallet toWallet = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new RuntimeException("Кошелек получателя с ID " + toWalletId + " не найден"));

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Недостаточно средств на кошельке отправителя");
        }

        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
    }

    @Transactional
    public void convertCurrency(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма для конвертации должна быть больше нуля");
        }

        Wallet fromWallet = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new RuntimeException("Кошелек-источник не найден"));

        Wallet toWallet = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new RuntimeException("Кошелек-получатель не найден"));

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Недостаточно средств для конвертации");
        }


        BigDecimal rate = getLiveExchangeRate(fromWallet.getCurrency(), toWallet.getCurrency());
        BigDecimal convertedAmount = amount.multiply(rate);

        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(convertedAmount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
    }

    // Получение актуального курса с внешнего открытого API с авто-резервом
    private BigDecimal getLiveExchangeRate(String from, String to) {
        if (from.equalsIgnoreCase(to)) {
            return BigDecimal.ONE;
        }

        try {
            String url = "https://api.exchangerate-api.com/v4/latest/" + from.toUpperCase();
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode rates = root.path("rates");

            if (rates.has(to.toUpperCase())) {
                return rates.get(to.toUpperCase()).decimalValue();
            }
        } catch (Exception e) {
            System.err.println("Не удалось получить курс из API, используем резервный фиксированный: " + e.getMessage());
        }


        return getFallbackExchangeRate(from, to);
    }

    private BigDecimal getFallbackExchangeRate(String from, String to) {
        if (from.equalsIgnoreCase("USD") && to.equalsIgnoreCase("RUB")) return new BigDecimal("90.0");
        if (from.equalsIgnoreCase("RUB") && to.equalsIgnoreCase("USD")) return new BigDecimal("0.011");
        if (from.equalsIgnoreCase("USD") && to.equalsIgnoreCase("EUR")) return new BigDecimal("0.92");
        if (from.equalsIgnoreCase("EUR") && to.equalsIgnoreCase("USD")) return new BigDecimal("1.09");
        if (from.equalsIgnoreCase("USD") && to.equalsIgnoreCase("KGS")) return new BigDecimal("87.5");
        if (from.equalsIgnoreCase("KGS") && to.equalsIgnoreCase("USD")) return new BigDecimal("0.0114");

        return BigDecimal.ONE;
    }
}