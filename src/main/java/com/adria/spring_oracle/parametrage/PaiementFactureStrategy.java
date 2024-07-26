package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamEmail;
import com.adria.spring_oracle.dao.Transaction_Type;

public class PaiementFactureStrategy implements EmailStrategy {
    @Override
    public ParamEmail createParamEmail(BankTransaction transaction) {
        ParamEmail paramEmail = new ParamEmail();
        paramEmail.setTransactionType(Transaction_Type.PF);
        paramEmail.setDestinataire(transaction.getBankAccount().getBankClient().getEmail());
        paramEmail.setEmetteur("chaimaakaine20@gmail.com");
        paramEmail.setObject("Confirmation de Paiement de Facture");
        paramEmail.setCorps(String.format(
                "Bonjour %s,<br/><br/>Votre paiement de facture de  %s avec la référence %s a été effectué .<br/><br/>Merci.<br/><img src='cid:image' alt='Image'><br/>",
                transaction.getBankAccount().getBankClient().getPrenom() + " " + transaction.getBankAccount().getBankClient().getNom(),
                transaction.getAmount() != null ? transaction.getAmount() : "type de chéquier inconnu",transaction.getReferenceFacture()!=null?transaction.getReferenceFacture():"reference not found"
        ));

        return paramEmail;
    }
}
