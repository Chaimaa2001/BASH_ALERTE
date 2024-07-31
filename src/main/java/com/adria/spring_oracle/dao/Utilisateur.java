package com.adria.spring_oracle.dao;

import jakarta.persistence.Column;

public abstract class Utilisateur {
    @Column(name = "bankClientID")
    private Long user_ID;
    private String nom;
    private String prenom;
    private String date_Naissance;
}
