package com.adria.spring_oracle.service;



import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SmsService {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        System.out.println("Twilio Account SID: " + accountSid);
        System.out.println("Twilio Auth Token: " + authToken);
        System.out.println("Twilio Phone Number: " + fromNumber);
        Twilio.init(accountSid, authToken);}

    public void sendSms(String to, String message) {
        Message.creator(new PhoneNumber(to), new PhoneNumber(fromNumber), message).create();
        System.out.println("Message sent to " + to);
    }
}


