package com.adria.spring_oracle.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class ParamEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private BankAccount bankAccount;
    private String destinataire;
    private String emetteur;
    private String object;
    private String corps;
    private Transaction_Type transactionType;
}
