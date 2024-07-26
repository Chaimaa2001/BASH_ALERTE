package com.adria.spring_oracle.config;

import com.adria.spring_oracle.parametrage.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EmailStrategyConfig {

    @Bean
    public Map<String, EmailStrategy> emailStrategies() {
        Map<String, EmailStrategy> strategies = new HashMap<>();
        // Assurez-vous que ces stratégies sont correctement créées et ajoutées
        strategies.put("V", new VirementEmailStrategy());
        strategies.put("DC", new ChequeRequestEmailStrategy());
        strategies.put("DE", new CreditRequestStrategy());
        strategies.put("DOC", new DemandeOppositionChequeStrategy());
        strategies.put("PF", new PaiementFactureStrategy());
        // Ajoutez d'autres stratégies selon le besoin
        return strategies;
    }
}
