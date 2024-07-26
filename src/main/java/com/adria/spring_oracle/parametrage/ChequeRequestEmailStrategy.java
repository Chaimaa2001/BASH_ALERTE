package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamEmail;
import com.adria.spring_oracle.dao.Transaction_Type;

public class ChequeRequestEmailStrategy implements EmailStrategy {
    @Override
    public ParamEmail createParamEmail(BankTransaction transaction) {
        ParamEmail paramEmail = new ParamEmail();
        paramEmail.setTransactionType(Transaction_Type.DC);
        paramEmail.setDestinataire(transaction.getBankAccount().getBankClient().getEmail());
        paramEmail.setEmetteur("chaimaakaine20@gmail.com");
        paramEmail.setObject("Confirmation de Demande de Chéquier");
        paramEmail.setCorps(String.format(
                "Bonjour %s,<br/><br/>Votre demande de chéquier de type %s a été traitée.<br/><br/>Merci.<br/><img src='cid:image' alt='Image'><br/>",
                transaction.getBankAccount().getBankClient().getPrenom() + " " + transaction.getBankAccount().getBankClient().getNom(),
                transaction.getTypeChequier() != null ? transaction.getTypeChequier() : "type de chéquier inconnu"
        ));
        return paramEmail;
    }
}
