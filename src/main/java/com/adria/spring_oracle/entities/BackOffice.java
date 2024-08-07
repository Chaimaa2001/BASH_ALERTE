package com.adria.spring_oracle.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@DiscriminatorValue("BACK_OFFICE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BackOffice extends Utilisateur {

    @Column(name = "userName")
    private String username;

    @Column(name = "passwordEncrepted")
    private String password;
}
