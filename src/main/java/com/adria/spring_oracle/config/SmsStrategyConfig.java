package com.adria.spring_oracle.config;

import com.adria.spring_oracle.parametrage.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SmsStrategyConfig {

    @Bean
    public Map<String, SmsStrategy> smsStrategies() {
        Map<String, SmsStrategy> strategies = new HashMap<>();
        strategies.put("V", new VirementSmsStrategy());
        strategies.put("DC", new ChequeRequestSmsStrategy());
        strategies.put("DE", new CreditRequestSmsStrategy());
        strategies.put("DOC", new DemandeOppositionChequeSmsStrategy());
        strategies.put("PF", new PaiementFactureSmsStrategy());
        // Ajoutez d'autres stratégies selon le besoin
        return strategies;
    }
}
