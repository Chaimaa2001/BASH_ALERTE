package com.adria.spring_oracle.service;

import com.infobip.ApiException;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
import com.adria.spring_oracle.dao.BankClient;
import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.Transaction_Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsService {

    @Autowired
    private SmsApi smsApi;

    @Value("${infobip.sender.name}")
    private String senderName;

    public void sendTransactionSms(BankTransaction bankTransaction) {
        BankClient bankClient = bankTransaction.getBankAccount().getBankClient();
        String to = bankClient.getPhoneNumber();
        String message = getSmsMessage(bankTransaction, bankClient);

        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Client phone number is not provided");
        }

        SmsTextualMessage smsMessage = new SmsTextualMessage()
                .from(senderName)
                .addDestinationsItem(new SmsDestination().to(to))
                .text(message);

        SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
                .messages(List.of(smsMessage));

        try {
            SmsResponse smsResponse = smsApi.sendSmsMessage(smsMessageRequest).execute();
            System.out.println("SMS envoyé avec succès! ID de message : " + smsResponse.getMessages().get(0).getMessageId());
        } catch (ApiException e) {
            System.out.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
        }
    }

    private String getSmsMessage(BankTransaction bankTransaction, BankClient bankClient) {
        String clientName = bankClient.getPrenom() + " " + bankClient.getNom();
        String amount = bankTransaction.getAmount() != null ? bankTransaction.getAmount().toString() : "montant inconnu";
        String reference = bankTransaction.getReferenceFacture() != null ? bankTransaction.getReferenceFacture() : "référence inconnue";
        String chequeType = bankTransaction.getTypeChequier() != null ? bankTransaction.getTypeChequier() : "type de chéquier inconnu";

        switch (bankTransaction.getTransactionType()) {
            case V:
                return String.format("Bonjour %s, votre virement de %s a été traité.", clientName, amount);
            case DC:
                return String.format("Bonjour %s, votre demande de chéquier de type %s a été traitée.", clientName, chequeType);
            case DE:
                return String.format("Bonjour %s, votre demande de crédit de %s a été approuvée.", clientName, amount);
            case DOC:
                return String.format("Bonjour %s, votre demande d'opposition sur chèque de %s a été prise en compte.", clientName, amount);
            case PF:
                return String.format("Bonjour %s, votre paiement de facture de %s avec la référence %s a été effectué.", clientName, amount, reference);
            default:
                return String.format("Bonjour %s, votre transaction a été traitée.", clientName);
        }
    }
}
