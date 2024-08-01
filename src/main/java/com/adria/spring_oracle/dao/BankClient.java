package com.adria.spring_oracle.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("BANK_CLIENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankClient extends Utilisateur {

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phoneNumber", unique = true)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "bankCode")
    private BankCode bankCode;

    @OneToMany(mappedBy = "bankClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankTransaction> bankTransactions = new ArrayList<>();
}
