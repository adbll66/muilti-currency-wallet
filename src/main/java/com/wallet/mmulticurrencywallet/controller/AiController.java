package com.wallet.mmulticurrencywallet.controller;

import com.wallet.mmulticurrencywallet.service.AiAdvisorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    private final AiAdvisorService aiAdvisorService;

    public AiController(AiAdvisorService aiAdvisorService) {
        this.aiAdvisorService = aiAdvisorService;
    }

    @GetMapping("/advice/{userId}")
    public ResponseEntity<String> getAdvice(@PathVariable Long userId) {
        String advice = aiAdvisorService.getAdviceForUser(userId);
        return ResponseEntity.ok(advice);
    }
}