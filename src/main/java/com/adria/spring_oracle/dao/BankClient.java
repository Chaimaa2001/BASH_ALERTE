package com.adria.spring_oracle.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BankClient {
    @Id
    private Long user_ID;
    private String nom;
    private String prenom;
    private String date_Naissance;
    private String email;
    private String phoneNumber;

    @Transient
    @OneToMany(mappedBy = "bankClient") // La relation est mappée par la propriété bankClient dans BankAccount
    private List<BankAccount> bankAccounts;
}
