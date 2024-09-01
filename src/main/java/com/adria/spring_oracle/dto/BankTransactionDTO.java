package com.adria.spring_oracle.dto;

import lombok.Data;
import java.util.Date;

@Data
public class BankTransactionDTO {
    private Long id;
    private Date transactionDate;
    private String transactionTypeDescription; // Champ pour la description
    private Double amount;
    private String typeChequier;
    private String referenceFacture;
    private String notificationMethod;
    private Long bankClientID;
    private String status; // Nouveau champ pour le statut

    // Constructeurs, getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTypeDescription() {
        return transactionTypeDescription;
    }

    public void setTransactionTypeDescription(String transactionTypeDescription) {
        this.transactionTypeDescription = transactionTypeDescription;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTypeChequier() {
        return typeChequier;
    }

    public void setTypeChequier(String typeChequier) {
        this.typeChequier = typeChequier;
    }

    public String getReferenceFacture() {
        return referenceFacture;
    }

    public void setReferenceFacture(String referenceFacture) {
        this.referenceFacture = referenceFacture;
    }

    public String getNotificationMethod() {
        return notificationMethod;
    }

    public void setNotificationMethod(String notificationMethod) {
        this.notificationMethod = notificationMethod;
    }

    public Long getBankClientID() {
        return bankClientID;
    }

    public void setBankClientID(Long bankClientID) {
        this.bankClientID = bankClientID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
