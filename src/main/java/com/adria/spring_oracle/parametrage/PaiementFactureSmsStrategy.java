package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamSMS;
import com.adria.spring_oracle.dao.Transaction_Type;

public class PaiementFactureSmsStrategy implements SmsStrategy {

    @Override
    public ParamSMS createParamSMS(BankTransaction transaction) {
        ParamSMS paramSMS = new ParamSMS();
        paramSMS.setTransactionType(Transaction_Type.PF);
        paramSMS.setDestinataire(transaction.getBankAccount().getBankClient().getPhoneNumber());
        paramSMS.setMessage(String.format(
                "Bonjour %s, votre paiement de facture de %s avec la référence %s a été effectué.",
                transaction.getBankAccount().getBankClient().getPrenom() + " " + transaction.getBankAccount().getBankClient().getNom(),
                transaction.getAmount() != null ? transaction.getAmount().toString() : "montant inconnu",
                transaction.getReferenceFacture() != null ? transaction.getReferenceFacture() : "référence inconnue"
        ));

        return paramSMS;
    }
}
