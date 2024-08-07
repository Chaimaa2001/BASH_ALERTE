package com.adria.spring_oracle.config;

import com.adria.spring_oracle.entities.BankTransaction;
import com.adria.spring_oracle.entities.Transaction_Type;
import com.adria.spring_oracle.repository.BankClientRepository;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Transactional
public class CustomBankTransactionFieldSetMapper extends BeanWrapperFieldSetMapper<BankTransaction> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy-HH:mm");

    @Autowired
    private BankClientRepository bankClientRepository;

    public CustomBankTransactionFieldSetMapper() {
        setTargetType(BankTransaction.class);
    }

    @Override
    public BankTransaction mapFieldSet(FieldSet fieldSet) {
        BankTransaction bankTransaction = new BankTransaction();
        try {
            bankTransaction.setId(fieldSet.readLong("transaction_id"));
            bankTransaction.setBankClientID(fieldSet.readLong("userID"));  // Utiliser userID pour récupérer le client
            bankTransaction.setStrTransactionDate(fieldSet.readString("transaction_date"));

            // Convertir strTransactionDate en Date
            Date transactionDate;
            try {
                transactionDate = DATE_FORMAT.parse(fieldSet.readString("transaction_date"));
                bankTransaction.setTransactionDate(transactionDate);
            } catch (ParseException e) {
                e.printStackTrace();
                bankTransaction.setTransactionDate(null); // ou une valeur par défaut
            }

            Transaction_Type transactionType = Transaction_Type.fromCode(fieldSet.readString("transaction_type"));
            bankTransaction.setTransactionType(transactionType);

            String amountStr = fieldSet.readString("transaction_amount");
            if (amountStr != null && !amountStr.isEmpty()) {
                bankTransaction.setAmount(Double.parseDouble(amountStr));
            } else {
                bankTransaction.setAmount(null); // ou une valeur par défaut
            }

            bankTransaction.setTypeChequier(fieldSet.readString("typeChequier"));
            bankTransaction.setReferenceFacture(fieldSet.readString("referenceFacture"));
            bankTransaction.setNotificationMethod(fieldSet.readString("notificationMethod"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bankTransaction;
    }
}
