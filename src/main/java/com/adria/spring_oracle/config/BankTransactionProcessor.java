package com.adria.spring_oracle.config;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.BankClient;
import com.adria.spring_oracle.repository.BankClientRepository;
import com.adria.spring_oracle.service.EmailService;
import com.adria.spring_oracle.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BankTransactionProcessor implements ItemProcessor<BankTransaction, BankTransaction> {

    private static final Logger logger = LoggerFactory.getLogger(BankTransactionProcessor.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
    private final EmailService emailService;
    private final SmsService smsService;
    private final BankClientRepository bankClientRepository;

    @Override
    @Transactional
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        try {
            bankTransaction.setTransactionDate(dateFormat.parse(bankTransaction.getStrTransactionDate()));
        } catch (ParseException e) {
            logger.error("Date parsing error for transaction: {}", bankTransaction, e);
            return null; // Ignore this transaction in case of date parsing error
        }

        // Fetch the client associated with the transaction
        Optional<BankClient> optionalBankClient = bankClientRepository.findById(bankTransaction.getBankClientID());
        if (optionalBankClient.isEmpty()) {
            logger.warn("Bank client not found for transaction: {}", bankTransaction);
            return null; // Ignore this transaction if the client is not found
        }

        BankClient bankClient = optionalBankClient.get();
        bankTransaction.setBankClient(bankClient);

        // Send notifications based on the notification method
        String notificationMethod = bankTransaction.getNotificationMethod();

        if (notificationMethod != null) {
            if (notificationMethod.equals("mail")) {
                Optional.ofNullable(bankClient.getEmail())
                        .filter(email -> !email.isEmpty())
                        .ifPresent(email -> {
                            try {
                                emailService.sendTransactionEmail(bankTransaction);
                            } catch (Exception e) {
                                logger.error("Failed to send email to: {}", email, e);
                            }
                        });
            } else if (notificationMethod.equals("sms")) {
                Optional.ofNullable(bankClient.getPhoneNumber())
                        .filter(phoneNumber -> !phoneNumber.isEmpty())
                        .ifPresent(phoneNumber -> {
                            try {
                                smsService.sendTransactionSms(bankTransaction);
                            } catch (Exception e) {
                                logger.error("Failed to send SMS to: {}", phoneNumber, e);
                            }
                        });
            } else if (notificationMethod.equals("mail&&sms")) {
                Optional.ofNullable(bankClient.getEmail())
                        .filter(email -> !email.isEmpty())
                        .ifPresent(email -> {
                            try {
                                emailService.sendTransactionEmail(bankTransaction);
                            } catch (Exception e) {
                                logger.error("Failed to send email to: {}", email, e);
                            }
                        });
                Optional.ofNullable(bankClient.getPhoneNumber())
                        .filter(phoneNumber -> !phoneNumber.isEmpty())
                        .ifPresent(phoneNumber -> {
                            try {
                                smsService.sendTransactionSms(bankTransaction);
                            } catch (Exception e) {
                                logger.error("Failed to send SMS to: {}", phoneNumber, e);
                            }
                        });
            } else {
                logger.warn("Unknown notification method specified for transaction: {}", bankTransaction);
            }
        } else {
            logger.warn("No notification method specified for transaction: {}", bankTransaction);
        }

        return bankTransaction;
    }
}
