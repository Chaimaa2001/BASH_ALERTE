package com.adria.spring_oracle.config;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.Transaction_Type;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CustomBankTransactionFieldSetMapper extends BeanWrapperFieldSetMapper<BankTransaction> {

    public CustomBankTransactionFieldSetMapper() {
        setTargetType(BankTransaction.class);
    }

    @Override
    public BankTransaction mapFieldSet(FieldSet fieldSet) {
        BankTransaction bankTransaction = new BankTransaction();
        try {
            bankTransaction.setId(fieldSet.readLong("transaction_id"));
            bankTransaction.setAccountID(fieldSet.readLong("account_number"));
            bankTransaction.setStrTransactionDate(fieldSet.readString("transaction_date"));
            bankTransaction.setTransactionType(Transaction_Type.valueOf(fieldSet.readString("transaction_type")));

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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
            Date transactionDate = sdf.parse(fieldSet.readString("transaction_date"));
            bankTransaction.setTransactionDate(transactionDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bankTransaction;
    }
}
