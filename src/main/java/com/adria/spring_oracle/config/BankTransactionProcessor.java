package com.adria.spring_oracle.config;

import com.adria.spring_oracle.dao.BankAccount;
import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.repository.BankAccountRepository;
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
 @Transactional
public class BankTransactionProcessor implements ItemProcessor<BankTransaction, BankTransaction> {

    private static final Logger logger = LoggerFactory.getLogger(BankTransactionProcessor.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
    private final BankAccountRepository bankAccountRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        try {
            bankTransaction.setTransactionDate(dateFormat.parse(bankTransaction.getStrTransactionDate()));
        } catch (ParseException e) {
            logger.error("Date parsing error for transaction: {}", bankTransaction, e);
            return null; // Ignore cette transaction en cas d'erreur de date
        }

        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(bankTransaction.getAccountID());
        if (optionalBankAccount.isEmpty()) {
            logger.warn("Bank account not found for transaction: {}", bankTransaction);
            return null; // Ignore cette transaction si le compte est introuvable
        }

        BankAccount bankAccount = optionalBankAccount.get();
        bankTransaction.setBankAccount(bankAccount);

        // Obtenez le type de notification
        String notificationMethod = bankTransaction.getNotificationMethod();
    // Récupérer le message depuis le service SMS

        // Envoyer des notifications selon le type de notification
        if (notificationMethod != null) {
            if (notificationMethod.equals("mail")) {
                Optional.ofNullable(bankAccount.getBankClient().getEmail())
                        .filter(email -> email != null && !email.isEmpty())
                        .ifPresent(email -> {
                            try {
                                emailService.sendTransactionEmail(bankTransaction);
                            } catch (Exception e) {
                                logger.error("Failed to send email to: {}", email, e);
                            }
                        });
            } else if (notificationMethod.equals("sms")) {
                Optional.ofNullable(bankAccount.getBankClient().getPhoneNumber())
                        .filter(phoneNumber -> phoneNumber != null && !phoneNumber.isEmpty())
                        .ifPresent(phoneNumber -> {
                            try {
                                smsService.sendTransactionSms(bankTransaction);
                            } catch (Exception e) {
                                logger.error("Failed to send SMS to: {}", phoneNumber, e);
                            }
                        });
            } else if (notificationMethod.equals("mail&&sms")) {
                Optional.ofNullable(bankAccount.getBankClient().getEmail())
                        .filter(email -> email != null && !email.isEmpty())
                        .ifPresent(email -> {
                            try {
                                emailService.sendTransactionEmail(bankTransaction);
                            } catch (Exception e) {
                                logger.error("Failed to send email to: {}", email, e);
                            }
                        });
                Optional.ofNullable(bankAccount.getBankClient().getPhoneNumber())
                        .filter(phoneNumber -> phoneNumber != null && !phoneNumber.isEmpty())
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
