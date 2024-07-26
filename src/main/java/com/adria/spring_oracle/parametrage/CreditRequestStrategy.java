package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamEmail;
import com.adria.spring_oracle.dao.Transaction_Type;

public class CreditRequestStrategy implements EmailStrategy {

    @Override
    public ParamEmail createParamEmail(BankTransaction transaction) {
        ParamEmail paramEmail = new ParamEmail();
        paramEmail.setTransactionType(Transaction_Type.DE);
        paramEmail.setDestinataire(transaction.getBankAccount().getBankClient().getEmail());
        paramEmail.setEmetteur("chaimaakaine20@gmail.com");
        paramEmail.setObject("Confirmation de Demande de Crédit");
        paramEmail.setCorps(String.format(
                "Bonjour %s,<br/><br/>Votre demande de crédit d'un montant de  %s a été traitée.<br/><br/>Merci.<br/><img src='cid:image' alt='Image'><br/>",
                transaction.getBankAccount().getBankClient().getPrenom() + " " + transaction.getBankAccount().getBankClient().getNom(),
                transaction.getAmount() != null ? transaction.getAmount() : "montant inconnu"
        ));
        return paramEmail;
    }
}
