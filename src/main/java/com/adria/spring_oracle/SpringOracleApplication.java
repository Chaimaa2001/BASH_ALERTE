package com.adria.spring_oracle;


import com.adria.spring_oracle.dao.BankClient;
import com.adria.spring_oracle.dao.BankCode;
import com.adria.spring_oracle.repository.BankClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
@Component
@SpringBootApplication
@Transactional

public class SpringOracleApplication /*implements CommandLineRunner*/ {



    @Autowired
    private BankClientRepository bankClientRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringOracleApplication.class, args);
    }

    /*@Override
    public void run(String... args) throws Exception {
        // Création des clients
        BankClient client1 = new BankClient(123L, "Kaine", "Chaimaa", "23-11-2001", "chaimaakaine20@gmail.com", "+2120649536980", BankCode.TIJJARI,null);
        BankClient client2 = new BankClient(456L, "Kaine", "Khadija", "23-11-2001", "chaimaakaine201@gmail.com", "+2120766802128", BankCode.BMCE,null);

        // Sauvegarde des clients
        bankClientRepository.save(client1);
        bankClientRepository.save(client2);



    }*/
}