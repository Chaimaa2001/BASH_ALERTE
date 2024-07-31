package com.adria.spring_oracle.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_client", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phoneNumber")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankClient {
    @Id
    @Column(name = "bankClientID")
    private Long user_ID;

    private String nom;
    private String prenom;
    private String date_Naissance;
    private String email;
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private BankCode bankCode;

    @OneToMany(mappedBy = "bankClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankTransaction> bankTransactions = new ArrayList<>();
}
