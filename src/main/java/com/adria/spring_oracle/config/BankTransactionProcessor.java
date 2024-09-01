package com.adria.spring_oracle.config;

import com.adria.spring_oracle.entities.BankTransaction;
import com.adria.spring_oracle.entities.BankClient;
import com.adria.spring_oracle.entities.NotificationStatus;
import com.adria.spring_oracle.entities.TransactionStatistics;
import com.adria.spring_oracle.repository.BankClientRepository;
import com.adria.spring_oracle.repository.TransactionStatisticsRepository;
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
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Transactional
public class BankTransactionProcessor implements ItemProcessor<BankTransaction, BankTransaction> {

    private static final Logger logger = LoggerFactory.getLogger(BankTransactionProcessor.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
    private final EmailService emailService;
    private final SmsService smsService;
    private final BankClientRepository bankClientRepository;
    private final TransactionStatisticsRepository statisticsRepository;

    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        TransactionStatistics statistics = new TransactionStatistics();
        statistics.setProcessingDate(LocalDateTime.now());
        statistics.setBankTransaction(bankTransaction);

        try {
            bankTransaction.setTransactionDate(dateFormat.parse(bankTransaction.getStrTransactionDate()));
        } catch (ParseException e) {
            logger.error("Erreur de parsing de la date pour la transaction: {}", bankTransaction, e);
            statistics.setSkippedTransactions(statistics.getSkippedTransactions() + 1);
            statisticsRepository.save(statistics);
            return null; // Ignorer cette transaction en cas d'erreur de parsing de la date
        }

        // Récupérer le client associé à la transaction
        Optional<BankClient> optionalBankClient = bankClientRepository.findById(bankTransaction.getBankClientID());
        if (optionalBankClient.isEmpty()) {
            logger.warn("Client bancaire non trouvé pour la transaction: {}", bankTransaction);
            statistics.setSkippedTransactions(statistics.getSkippedTransactions() + 1);
            statisticsRepository.save(statistics);
            return null; // Ignorer cette transaction si le client n'est pas trouvé
        }

        BankClient bankClient = optionalBankClient.get();
        bankTransaction.setBankClient(bankClient);

        // Initialisation des indicateurs de succès
        AtomicBoolean emailSuccess = new AtomicBoolean(false);
        AtomicBoolean smsSuccess = new AtomicBoolean(false);

        // Envoyer les notifications selon la méthode spécifiée
        String notificationMethod = bankTransaction.getNotificationMethod();
        statistics.setTotalTransactions(statistics.getTotalTransactions() + 1);

        if (notificationMethod != null) {
            if (notificationMethod.equals("mail")) {
                Optional.ofNullable(bankClient.getEmail())
                        .filter(email -> !email.isEmpty())
                        .ifPresentOrElse(email -> {
                            try {
                                emailService.sendTransactionEmail(bankTransaction);
                                statistics.setSuccessfulEmails(statistics.getSuccessfulEmails() + 1);
                                emailSuccess.set(true);
                            } catch (Exception e) {
                                logger.error("Échec de l'envoi de l'email à: {}", email, e);
                                statistics.setFailedEmails(statistics.getFailedEmails() + 1);
                            }
                        }, () -> statistics.setFailedEmails(statistics.getFailedEmails() + 1));
            } else if (notificationMethod.equals("sms")) {
                Optional.ofNullable(bankClient.getPhoneNumber())
                        .filter(phoneNumber -> !phoneNumber.isEmpty())
                        .ifPresentOrElse(phoneNumber -> {
                            try {
                                smsService.sendTransactionSms(bankTransaction);
                                statistics.setSuccessfulSms(statistics.getSuccessfulSms() + 1);
                                smsSuccess.set(true);
                            } catch (Exception e) {
                                logger.error("Échec de l'envoi du SMS à: {}", phoneNumber, e);
                                statistics.setFailedSms(statistics.getFailedSms() + 1);
                            }
                        }, () -> statistics.setFailedSms(statistics.getFailedSms() + 1));
            } else if (notificationMethod.equals("mail&&sms")) {
                // Gérer l'envoi de l'email et du SMS avec les statistiques appropriées
                Optional.ofNullable(bankClient.getEmail())
                        .filter(email -> !email.isEmpty())
                        .ifPresentOrElse(email -> {
                            try {
                                emailService.sendTransactionEmail(bankTransaction);
                                statistics.setSuccessfulEmails(statistics.getSuccessfulEmails() + 1);
                                emailSuccess.set(true);
                            } catch (Exception e) {
                                logger.error("Échec de l'envoi de l'email à: {}", email, e);
                                statistics.setFailedEmails(statistics.getFailedEmails() + 1);
                            }
                        }, () -> statistics.setFailedEmails(statistics.getFailedEmails() + 1));
                Optional.ofNullable(bankClient.getPhoneNumber())
                        .filter(phoneNumber -> !phoneNumber.isEmpty())
                        .ifPresentOrElse(phoneNumber -> {
                            try {
                                smsService.sendTransactionSms(bankTransaction);
                                statistics.setSuccessfulSms(statistics.getSuccessfulSms() + 1);
                                smsSuccess.set(true);
                            } catch (Exception e) {
                                logger.error("Échec de l'envoi du SMS à: {}", phoneNumber, e);
                                statistics.setFailedSms(statistics.getFailedSms() + 1);
                            }
                        }, () -> statistics.setFailedSms(statistics.getFailedSms() + 1));
            } else {
                logger.warn("Méthode de notification inconnue spécifiée pour la transaction: {}", bankTransaction);
            }
        } else {
            logger.warn("Aucune méthode de notification spécifiée pour la transaction: {}", bankTransaction);
        }

        // Déterminer le statut final de la notification
        if (notificationMethod.equals("mail")) {
            bankTransaction.setNotificationStatus(NotificationStatus.valueOf(emailSuccess.get() ? "SUCCESS" : "FAILED"));
        } else if (notificationMethod.equals("sms")) {
            bankTransaction.setNotificationStatus(NotificationStatus.valueOf(smsSuccess.get() ? "SUCCESS" : "FAILED"));
        } else if (notificationMethod.equals("mail&&sms")) {
            bankTransaction.setNotificationStatus(NotificationStatus.valueOf((emailSuccess.get() && smsSuccess.get()) ? "SUCCESS" : "FAILED"));
        }

        statisticsRepository.save(statistics);
        return bankTransaction;
    }
}
