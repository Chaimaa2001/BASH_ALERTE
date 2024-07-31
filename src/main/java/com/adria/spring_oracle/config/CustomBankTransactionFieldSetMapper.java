package com.adria.spring_oracle.config;

import com.adria.spring_oracle.dao.BankClient;
import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.Transaction_Type;
import com.adria.spring_oracle.repository.BankClientRepository;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
@Transactional
public class CustomBankTransactionFieldSetMapper extends BeanWrapperFieldSetMapper<BankTransaction> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy-HH:mm");

    @Autowired
    private BankClientRepository bankClientRepository; // Correction ici pour utiliser BankClientRepository

    public CustomBankTransactionFieldSetMapper() {
        setTargetType(BankTransaction.class);
    }

    @Override
    public BankTransaction mapFieldSet(FieldSet fieldSet) {
        BankTransaction bankTransaction = new BankTransaction();
        try {
            bankTransaction.setId(fieldSet.readLong("transaction_id"));

            // Récupérer le BankClient en fonction de l'ID
            bankTransaction.setBankClientID( fieldSet.readLong("bank_client"));

            bankTransaction.setStrTransactionDate(fieldSet.readString("transaction_date"));

            // Utilisation du code de transaction pour déterminer Transaction_Type
            Transaction_Type transactionType = Transaction_Type.fromCode(fieldSet.readString("transaction_type"));
            bankTransaction.setTransactionType(transactionType);

            String amountStr = fieldSet.readString("transaction_amount");
            if (amountStr != null && !amountStr.isEmpty()) {
                bankTransaction.setAmount(Double.parseDouble(amountStr));
            } else {
                bankTransaction.setAmount(null); // ou une valeur par défaut si nécessaire
            }

            // Lire typeChequier en tant que String
            String typeChequierStr = fieldSet.readString("typeChequier");
            bankTransaction.setTypeChequier(typeChequierStr != null && !typeChequierStr.isEmpty() ? typeChequierStr : null);

            bankTransaction.setReferenceFacture(fieldSet.readString("referenceFacture"));
            bankTransaction.setNotificationMethod(fieldSet.readString("notificationMethod"));

            // Convertir strTransactionDate en Date
            Date transactionDate;
            try {
                transactionDate = DATE_FORMAT.parse(fieldSet.readString("transaction_date"));
                bankTransaction.setTransactionDate(transactionDate);
            } catch (ParseException e) {
                e.printStackTrace();
                // Vous pouvez également gérer cette exception en la journalisant ou en lançant une exception personnalisée
                bankTransaction.setTransactionDate(null); // ou une valeur par défaut
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bankTransaction;
    }
}
