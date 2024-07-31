package com.adria.spring_oracle.service;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamSMS;
import com.adria.spring_oracle.dao.BankCode;
import com.adria.spring_oracle.dao.Transaction_Type;
import com.adria.spring_oracle.repository.ParamSMSRepository;
import com.infobip.ApiException;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private final SmsApi smsApi;
    private final ParamSMSRepository repository;

    @Value("${infobip.sender.name}")
    private String senderName;

    public void sendTransactionSms(BankTransaction bankTransaction) {
        // Récupérer les paramètres SMS en fonction du BankCode et du type de transaction
        BankCode bankCode = bankTransaction.getBankClient().getBankCode();
        Transaction_Type transactionType = bankTransaction.getTransactionType();

        List<ParamSMS> paramSMSList = repository.findByBankCodeAndTransactionType(bankCode, transactionType);
        if (paramSMSList.isEmpty()) {
            throw new IllegalArgumentException("No SMS parameters found for bank code: " + bankCode +
                    " and transaction type: " + transactionType);
        }

        // Vous pouvez choisir ici comment gérer plusieurs ParamSMS (par exemple, utiliser le premier)
        ParamSMS paramSMS = paramSMSList.get(0); // Choisir le premier ou adapter selon votre logique

        String to = bankTransaction.getBankClient().getPhoneNumber();
        String message = paramSMS.getMessage()
                .replace("{prenom}", bankTransaction.getBankClient().getPrenom() != null ? bankTransaction.getBankClient().getPrenom() : "N/A")
                .replace("{nom}", bankTransaction.getBankClient().getNom() != null ? bankTransaction.getBankClient().getNom() : "N/A")
                .replace("{transactionType}", transactionType.name())
                .replace("{montant}", bankTransaction.getAmount() != null ? bankTransaction.getAmount().toString() : "N/A")
                .replace("{typeChequier}", bankTransaction.getTypeChequier() != null ? bankTransaction.getTypeChequier() : "N/A")
                .replace("{referenceFacture}", bankTransaction.getReferenceFacture() != null ? bankTransaction.getReferenceFacture() : "N/A");

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
            logger.info("SMS envoyé avec succès! ID de message : " + smsResponse.getMessages().get(0).getMessageId());
        } catch (ApiException e) {
            logger.error("Erreur lors de l'envoi du SMS : " + e.getMessage(), e);
        }
    }
}
