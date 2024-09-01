package com.adria.spring_oracle.controller;

import com.adria.spring_oracle.dto.BankTransactionDTO;
import com.adria.spring_oracle.entities.BankCode;
import com.adria.spring_oracle.entities.BankTransaction;
import com.adria.spring_oracle.mappers.BankTransactionService;
import com.adria.spring_oracle.repository.BankTransactionRepository;
import com.adria.spring_oracle.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class BankTransactionController {


    private final EmailService emailService;
    private final String adminEmail = "chaimaakaine20@gmail.com";
    private final BankTransactionService bankTransactionService;
    @GetMapping("/list")
    public List<BankTransactionDTO> getAllTransactions() {
        return bankTransactionService.findAll();
    }
    @GetMapping("/auth")
    public Authentication authentication(Authentication authentication){

        return authentication;
    }

    @GetMapping("/by-bank-code")
    public List<BankTransactionDTO> getTransactionsByBankCode(@RequestParam BankCode bankCode) {
        return bankTransactionService.findByBankCode(bankCode);
    }
    @GetMapping("/filters")
    public List<String> getTransactionTypes() {
        return bankTransactionService.findDistinctTransactionTypes();
    }
    @PostMapping("/notify-failure/{transactionId}")
    public ResponseEntity<String> notifyFailure(@PathVariable Long transactionId) {
        String subject = "Transaction Failed";
        String body = "La transaction avec l'ID " + transactionId + " n'a pas été envoyée.";

        try {
            emailService.notifyAdmin(transactionId,subject);
            return ResponseEntity.ok("Notification envoyée à l'administrateur.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoi de l'email.");
        }
    }
}
