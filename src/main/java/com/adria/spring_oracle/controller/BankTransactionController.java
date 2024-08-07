package com.adria.spring_oracle.controller;

import com.adria.spring_oracle.dto.BankTransactionDTO;
import com.adria.spring_oracle.entities.BankTransaction;
import com.adria.spring_oracle.mappers.BankTransactionService;
import com.adria.spring_oracle.repository.BankTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class BankTransactionController {


    private final BankTransactionService bankTransactionService;
    @GetMapping("/list")
    public List<BankTransactionDTO> getAllTransactions() {
        return bankTransactionService.findAll();
    }
    @GetMapping("/auth")
    public Authentication authentication(Authentication authentication){
        return authentication;
    }
}
