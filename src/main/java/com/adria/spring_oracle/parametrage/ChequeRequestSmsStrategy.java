package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamSMS;
import com.adria.spring_oracle.dao.Transaction_Type;

public class ChequeRequestSmsStrategy implements SmsStrategy {

    @Override
    public ParamSMS createParamSMS(BankTransaction transaction) {
        ParamSMS paramSMS = new ParamSMS();
        paramSMS.setTransactionType(Transaction_Type.DC);
        paramSMS.setDestinataire(transaction.getBankAccount().getBankClient().getPhoneNumber());
        paramSMS.setMessage(String.format(
                "Bonjour %s, votre demande de chéquier de type %s a été traitée.",
                transaction.getBankAccount().getBankClient().getPrenom() + " " + transaction.getBankAccount().getBankClient().getNom(),
                transaction.getTypeChequier() != null ? transaction.getTypeChequier() : "type de chéquier inconnu"
        ));

        return paramSMS;
    }
}
