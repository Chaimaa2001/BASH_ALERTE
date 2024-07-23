package com.adria.spring_oracle.service;

import com.infobip.ApiException;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
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

    public void sendSms(String to, String message) {
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
}