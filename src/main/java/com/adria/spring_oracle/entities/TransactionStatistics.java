package com.adria.spring_oracle.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime processingDate;

    private int totalTransactions;

    private int successfulEmails;

    private int failedEmails;

    private int successfulSms;

    private int failedSms;

    private int skippedTransactions;

    @ManyToOne
    private BankTransaction bankTransaction;

    // Ajoutez d'autres champs si nécessaire
}
