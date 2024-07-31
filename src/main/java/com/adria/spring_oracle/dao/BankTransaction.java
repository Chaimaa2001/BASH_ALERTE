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
    @JoinColumn(name = "bankClientID", referencedColumnName = "bankClientID")  // Spécifie la colonne qui fait référence à BankClient
    private BankClient bankClient;

    @Transient
    private Long bankClientID;

    private Date transactionDate;

    @Transient
    private String strTransactionDate;

    @Enumerated(value = EnumType.STRING)
    private Transaction_Type transactionType;

    private Double amount;
    private String typeChequier;
    private String referenceFacture;
    private String notificationMethod; // Utiliser un champ plus descriptif
}
