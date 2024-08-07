package com.adria.spring_oracle.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParamSMS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "BANK_CODE")
    @Enumerated(value = EnumType.STRING)
    private BankCode bankCode;
    private String destinataire;
    private String message;
    @Column(name = "TRANSACTION_TYPE")
    @Enumerated(value = EnumType.STRING)
    private Transaction_Type transactionType;
}
