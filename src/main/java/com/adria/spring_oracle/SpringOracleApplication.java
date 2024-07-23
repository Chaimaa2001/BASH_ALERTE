package com.adria.spring_oracle;


import com.adria.spring_oracle.dao.BankAccount;
import com.adria.spring_oracle.dao.BankClient;
import com.adria.spring_oracle.repository.BankAccountRepository;
import com.adria.spring_oracle.repository.BankClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
@SpringBootApplication

public class SpringOracleApplication implements CommandLineRunner {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankClientRepository bankClientRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringOracleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Création des clients
        BankClient client1 = new BankClient(123L, "Kaine", "Chaimaa", "23-11-2001", "chaimaakaine20@gmail.com", "+2120649536980", null);
        BankClient client2 = new BankClient(456L, "Kaine", "Khadija", "23-11-2001", "chaimaakaine201@gmail.com", "+2120766802128", null);

        // Sauvegarde des clients
        bankClientRepository.save(client1);
        bankClientRepository.save(client2);

        // Création des comptes bancaires et association avec les clients
        BankAccount account1 = new BankAccount(33214566L, 10000, client1);
        BankAccount account2 = new BankAccount(33214567L, 15000, client2);

        // Sauvegarde des comptes bancaires
        bankAccountRepository.save(account1);
        bankAccountRepository.save(account2);

        // Mise à jour des clients avec leurs comptes respectifs
        client1.setBankAccounts(Arrays.asList(account1));
        client2.setBankAccounts(Arrays.asList(account2));

        // Mise à jour des clients dans la base de données
        bankClientRepository.save(client1);
        bankClientRepository.save(client2);
    }
}