package com.adria.spring_oracle.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    @JsonIgnore
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
    private String notificationMethod;
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;


}
