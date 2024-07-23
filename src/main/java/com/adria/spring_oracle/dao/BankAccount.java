package com.adria.spring_oracle.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BankAccount {
    @Id
    private Long accountID;
    private double Montant;

    @ManyToOne
    @JoinColumn(name = "user_ID") // La clé étrangère dans BankAccount fait référence à BankClient
    private BankClient bankClient;
}
