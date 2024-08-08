package com.adria.spring_oracle;


import com.adria.spring_oracle.entities.BackOffice;
import com.adria.spring_oracle.keycloak.KeycloakUserImporter;
import com.adria.spring_oracle.keycloak.OracleUserImporter;
import com.adria.spring_oracle.repository.BankClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@SpringBootApplication
@Transactional

public class SpringOracleApplication implements CommandLineRunner {


    @Autowired
    private BankClientRepository bankClientRepository;

    @Autowired
    private KeycloakUserImporter keycloakUserImporter;

    @Autowired
    private OracleUserImporter oracleUserImporter;


    public static void main(String[] args) {
        SpringApplication.run(SpringOracleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Lire les utilisateurs depuis la base de données
        List<BackOffice> users = oracleUserImporter.getUsers();

        // Importer les utilisateurs dans Keycloak
        keycloakUserImporter.importUsers(users);

        System.out.println("Utilisateurs importés avec succès dans Keycloak !");
    }
}