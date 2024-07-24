package com.adria.spring_oracle.config;

import com.adria.spring_oracle.dao.BankAccount;
import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.Transaction_Type;
import com.adria.spring_oracle.repository.BankAccountRepository;
import com.adria.spring_oracle.service.EmailService;
import com.adria.spring_oracle.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Component
@RequiredArgsConstructor
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

        // Obtenez le message en fonction du type de transaction et des champs disponibles
        String text = getTransactionMessage(bankTransaction);

        // Envoyer des notifications
        switch (bankTransaction.getNotificationMethod()) {
            case "mail":
                Optional.ofNullable(bankAccount.getBankClient().getEmail())
                        .filter(email -> email != null && !email.isEmpty())
                        .ifPresent(email -> {
                            try {
                                emailService.sendEmail(email, "Transaction Notification", text);
                            } catch (Exception e) {
                                logger.error("Failed to send email to: {}", email, e);
                            }
                        });
                break;

            case "sms":
                Optional.ofNullable(bankAccount.getBankClient().getPhoneNumber())
                        .filter(phoneNumber -> phoneNumber != null && !phoneNumber.isEmpty())
                        .ifPresent(phoneNumber -> {
                            try {
                                smsService.sendSms(phoneNumber, text);
                            } catch (Exception e) {
                                logger.error("Failed to send SMS to: {}", phoneNumber, e);
                            }
                        });
                break;

            case "mail&&sms":
                Optional.ofNullable(bankAccount.getBankClient().getEmail())
                        .filter(email -> email != null && !email.isEmpty())
                        .ifPresent(email -> {
                            try {
                                emailService.sendEmail(email, "Transaction Notification", text);
                            } catch (Exception e) {
                                logger.error("Failed to send email to: {}", email, e);
                            }
                        });

                Optional.ofNullable(bankAccount.getBankClient().getPhoneNumber())
                        .filter(phoneNumber -> phoneNumber != null && !phoneNumber.isEmpty())
                        .ifPresent(phoneNumber -> {
                            try {
                                smsService.sendSms(phoneNumber, text);
                            } catch (Exception e) {
                                logger.error("Failed to send SMS to: {}", phoneNumber, e);
                            }
                        });
                break;

            default:
                logger.warn("Unexpected notification method: {}", bankTransaction.getNotificationMethod());
                break;
        }

        return bankTransaction;
    }

    // Méthode pour obtenir le message en fonction du type de transaction et des champs disponibles
    private String getTransactionMessage(BankTransaction bankTransaction) {
        String amount = bankTransaction.getAmount() != null ? bankTransaction.getAmount().toString() : "montant inconnu";
        String reference = bankTransaction.getReferenceFacture() != null ? bankTransaction.getReferenceFacture() : "référence inconnue";
        String chequeType = bankTransaction.getTypeChequier() != null ? bankTransaction.getTypeChequier() : "type de chéquier inconnu";

        switch (bankTransaction.getTransactionType()) {
            case Virement:
                return "Votre virement de " + amount + " a été traité.";
            case DEMANDE_CHEQUIER:
                return "Votre demande de chéquier de type " + chequeType + " a été traitée.";
            case DEMANDE_CREDIT:
                return "Votre demande de crédit de " + amount + " a été approuvée.";
            case DEMANDE_OPPOSITION_CHEQUE:
                return "Votre demande d'opposition sur chèque de " + amount + " a été prise en compte.";
            case PAIEMENT_FACTURE:
                return "Votre paiement de facture de " + amount + " avec la référence " + reference + " a été effectué.";
            default:
                return "Votre transaction a été traitée.";
        }
    }
}
