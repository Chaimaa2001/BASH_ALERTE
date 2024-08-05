package com.adria.spring_oracle.controller;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.repository.BankTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/transactions")
public class BankTransactionController {

    @Autowired
    private BankTransactionRepository bankTransactionService;

    @GetMapping("/list")
    public List<BankTransaction> getAllTransactions() {
        return bankTransactionService.findAll();
    }
}
