package com.adria.spring_oracle.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CLIENT_TYPE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Utilisateur {

    @Id
    @Column(name = "userID")
    private Long userID;

    @Column(name = "userFirstName")
    private String nom;

    @Column(name = "userLastName")
    private String prenom;

    @Column(name = "dateNaissance")
    private String dateNaissance;
}
