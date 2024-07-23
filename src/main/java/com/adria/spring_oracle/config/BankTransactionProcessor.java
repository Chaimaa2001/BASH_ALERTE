package com.adria.spring_oracle.config;

import com.adria.spring_oracle.dao.BankAccount;
import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.repository.BankAccountRepository;
import com.adria.spring_oracle.service.EmailService;
import com.adria.spring_oracle.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

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

        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(bankTransaction.getAccountID());
        if (optionalBankAccount.isEmpty()) {
            return null; // Ignore cette transaction si le compte est introuvable
        }

        BankAccount bankAccount = optionalBankAccount.get();
        bankTransaction.setBankAccount(bankAccount);

        String subject = "Transaction Notification";
        String text = "Your transaction of " + bankTransaction.getAmount() + " has been processed.";

        switch (bankTransaction.getNotificationMethod().toLowerCase()) {
            case "email":
                Optional.ofNullable(bankAccount.getBankClient().getEmail())
                        .filter(email -> !email.isEmpty())
                        .ifPresent(email -> {
                            try {
                                emailService.sendEmail(email, subject, text);
                            } catch (Exception e) {
                                e.printStackTrace(); // Log the exception if email sending fails
                            }
                        });
                break;

            case "sms":
                Optional.ofNullable(bankAccount.getBankClient().getPhoneNumber())
                        .filter(phoneNumber -> !phoneNumber.isEmpty())
                        .ifPresent(phoneNumber -> {
                            try {
                                smsService.sendSms(phoneNumber, text);
                            } catch (Exception e) {
                                e.printStackTrace(); // Log the exception if SMS sending fails
                            }
                        });
                break;

            default:
                // Handle unexpected notification methods if necessary
                break;
        }

        return bankTransaction;
    }
}
