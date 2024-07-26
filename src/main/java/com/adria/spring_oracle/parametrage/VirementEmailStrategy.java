package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamEmail;
import com.adria.spring_oracle.dao.Transaction_Type;

public class VirementEmailStrategy implements EmailStrategy {

    @Override
    public ParamEmail createParamEmail(BankTransaction transaction) {
        ParamEmail paramEmail = new ParamEmail();
        paramEmail.setTransactionType(Transaction_Type.V);
        paramEmail.setDestinataire(transaction.getBankAccount().getBankClient().getEmail());
        paramEmail.setEmetteur("chaimaakaine20@gmail.coù");
        paramEmail.setObject("Confirmation de Virement");
        paramEmail.setCorps(String.format(
                "Bonjour %s,<br/><br/>Vous avez effectué un virement de %s.<br/><br/>Merci.<br/><img src='cid:image' alt='Image'><br/>",
                transaction.getBankAccount().getBankClient().getPrenom() + " " + transaction.getBankAccount().getBankClient().getNom(),
                transaction.getAmount() != null ? transaction.getAmount().toString() : "montant inconnu"
        ));

        return paramEmail;
    }
}
