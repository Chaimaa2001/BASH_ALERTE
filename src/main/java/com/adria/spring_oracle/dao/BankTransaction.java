package com.adria.spring_oracle.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankTransaction {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountID", referencedColumnName = "accountID")
    private BankAccount bankAccount;

    @Transient
    private Long accountID;

    private Date transactionDate;

    @Transient
    private String strTransactionDate;

    private String transactionType;
    private double amount;
    private String notificationMethod; // Utiliser un champ plus descriptif
}
