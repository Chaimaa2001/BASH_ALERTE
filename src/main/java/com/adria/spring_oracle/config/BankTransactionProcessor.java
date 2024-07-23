package com.adria.spring_oracle.config;

import com.adria.spring_oracle.dao.BankAccount;
import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.repository.BankAccountRepository;
import com.adria.spring_oracle.service.EmailService;
import com.adria.spring_oracle.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
@RequiredArgsConstructor
public class BankTransactionProcessor implements ItemProcessor<BankTransaction, BankTransaction> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
    private final BankAccountRepository bankAccountRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        try {
            bankTransaction.setTransactionDate(dateFormat.parse(bankTransaction.getStrTransactionDate()));
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Ignore cette transaction en cas d'erreur de date
        }

        BankAccount bankAccount = bankAccountRepository.findById(bankTransaction.getAccountID()).orElse(null);
        if (bankAccount == null) {
            return null; // Ignore cette transaction si le compte est introuvable
        }

        bankTransaction.setBankAccount(bankAccount);

        String subject = "Transaction Notification";
        String text = "Your transaction of " + bankTransaction.getAmount() + " has been processed.";

        if ("email".equalsIgnoreCase(bankTransaction.getNotificationMethod())) {
            String email = bankAccount.getBankClient().getEmail();
            if (email != null && !email.isEmpty()) {
                emailService.sendEmail(email, subject, text);
            }
        } else if ("sms".equalsIgnoreCase(bankTransaction.getNotificationMethod())) {
            String phoneNumber = bankAccount.getBankClient().getPhoneNumber();
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                smsService.sendSms(phoneNumber, text);
            }
        }

        return bankTransaction;
    }
}
