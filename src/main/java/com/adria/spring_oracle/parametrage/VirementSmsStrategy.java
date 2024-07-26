package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamSMS;
import com.adria.spring_oracle.dao.Transaction_Type;



public class VirementSmsStrategy implements SmsStrategy {

    @Override
    public ParamSMS createParamSMS(BankTransaction transaction) {
        ParamSMS paramSMS = new ParamSMS();
        paramSMS.setTransactionType(Transaction_Type.V);
        paramSMS.setDestinataire(transaction.getBankAccount().getBankClient().getPhoneNumber());
        paramSMS.setMessage(String.format(
                "Bonjour %s, votre virement de %s a été traité.",
                transaction.getBankAccount().getBankClient().getPrenom() + " " + transaction.getBankAccount().getBankClient().getNom(),
                transaction.getAmount() != null ? transaction.getAmount().toString() : "montant inconnu"
        ));

        return paramSMS;
    }
}
