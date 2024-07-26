package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamSMS;
import com.adria.spring_oracle.dao.Transaction_Type;

public class DemandeOppositionChequeSmsStrategy implements SmsStrategy {

    @Override
    public ParamSMS createParamSMS(BankTransaction transaction) {
        ParamSMS paramSMS = new ParamSMS();
        paramSMS.setTransactionType(Transaction_Type.DOC);
        paramSMS.setDestinataire(transaction.getBankAccount().getBankClient().getPhoneNumber());
        paramSMS.setMessage(String.format(
                "Bonjour %s, votre demande d'opposition sur chèque de %s a été traitée.",
                transaction.getBankAccount().getBankClient().getPrenom() + " " + transaction.getBankAccount().getBankClient().getNom(),
                transaction.getAmount() != null ? transaction.getAmount().toString() : "montant inconnu"
        ));

        return paramSMS;
    }
}
