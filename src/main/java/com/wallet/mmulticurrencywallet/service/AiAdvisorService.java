package com.wallet.mmulticurrencywallet.service;

import com.wallet.mmulticurrencywallet.model.Wallet;
import com.wallet.mmulticurrencywallet.repository.WalletRepository;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiAdvisorService {

    private final ChatClient chatClient;
    private final WalletRepository walletRepository;

    public AiAdvisorService(ChatClient chatClient, WalletRepository walletRepository) {
        this.chatClient = chatClient;
        this.walletRepository = walletRepository;
    }

    public String getAdviceForUser(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        if (wallets.isEmpty()) {
            return "У вас пока нет открытых счетов для анализа.";
        }

        StringBuilder sb = new StringBuilder("У пользователя следующие счета и балансы:\n");
        for (Wallet w : wallets) {
            sb.append("- ").append(w.getCurrency()).append(": ").append(w.getBalance()).append("\n");
        }
        sb.append("Дай короткий, профессиональный и полезный финансовый совет на русском языке по управлению этими накоплениями.");

        return chatClient.call(sb.toString());
    }
}