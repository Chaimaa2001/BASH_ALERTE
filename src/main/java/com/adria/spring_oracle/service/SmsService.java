package com.adria.spring_oracle.service;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamSMS;
import com.adria.spring_oracle.parametrage.EmailStrategy;
import com.adria.spring_oracle.repository.ParamSMSRepository;
import com.adria.spring_oracle.parametrage.SmsStrategy;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private final SmsApi smsApi;
    private final Map<String, SmsStrategy> smsStrategies; // Injection des stratégies
    private final ParamSMSRepository repository;

    @Value("${infobip.sender.name}")
    private String senderName;

    public void sendTransactionSms(BankTransaction bankTransaction) {
        String transactionType = bankTransaction.getTransactionType().name();
        SmsStrategy strategy = smsStrategies.get(transactionType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for transaction type: " + transactionType);
        }
        ParamSMS paramSMS=strategy.createParamSMS(bankTransaction);

        repository.save(paramSMS);

        String to = paramSMS.getDestinataire();
        String message = paramSMS.getMessage();

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
