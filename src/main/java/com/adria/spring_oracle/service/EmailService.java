package com.adria.spring_oracle.service;

import com.adria.spring_oracle.dao.BankClient;
import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.Transaction_Type;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendTransactionEmail(BankTransaction bankTransaction) {
        BankClient bankClient = bankTransaction.getBankAccount().getBankClient();
        String to = bankClient.getEmail();
        String subject = getEmailSubject(bankTransaction.getTransactionType());
        String body = getEmailBody(bankTransaction, bankClient);

        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Client email is not provided");
        }

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);  // 'true' indicates HTML content

            // Ajouter une image en tant que ressource embarquée
            File imageFile = new File("src/main/resources/static/img.png");
            if (imageFile.exists()) {
                helper.addInline("image", imageFile);
            } else {
                System.err.println("Image file not found: " + imageFile.getAbsolutePath());
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String getEmailSubject(Transaction_Type transactionType) {
        switch (transactionType) {
            case V:
                return "Confirmation de Virement";
            case DC:
                return "Confirmation de Demande de Chéquier";
            case DE:
                return "Confirmation de Demande de Crédit";
            case DOC:
                return "Confirmation de Demande d'Opposition sur Chèque";
            case PF:
                return "Confirmation de Paiement de Facture";
            default:
                return "Notification de Transaction";
        }
    }

    private String getEmailBody(BankTransaction bankTransaction, BankClient bankClient) {
        String clientName = bankClient.getPrenom() + " " + bankClient.getNom();
        String amount = bankTransaction.getAmount() != null ? bankTransaction.getAmount().toString() : "montant inconnu";
        String reference = bankTransaction.getReferenceFacture() != null ? bankTransaction.getReferenceFacture() : "référence inconnue";
        String chequeType = bankTransaction.getTypeChequier() != null ? bankTransaction.getTypeChequier() : "type de chéquier inconnu";

        String imageCid = "image";  // ID pour l'image dans le corps du message

        switch (bankTransaction.getTransactionType()) {
            case V:
                return String.format(
                        "Bonjour %s,<br/><br/>Vous avez effectué un virement de %s.<br/><br/>Merci.<br/><img src='cid:%s' alt='Image'><br/>",
                        clientName, amount, imageCid);
            case DC:
                return String.format(
                        "Bonjour %s,<br/><br/>Votre demande de chéquier de type %s a été traitée.<br/><br/>Merci.<br/><img src='cid:%s' alt='Image'><br/>",
                        clientName, chequeType, imageCid);
            case DE:
                return String.format(
                        "Bonjour %s,<br/><br/>Votre demande de crédit de %s a été approuvée.<br/><br/>Merci.<br/><img src='cid:%s' alt='Image'><br/>",
                        clientName, amount, imageCid);
            case DOC:
                return String.format(
                        "Bonjour %s,<br/><br/>Votre demande d'opposition sur chèque de %s a été prise en compte.<br/><br/>Merci.<br/><img src='cid:%s' alt='Image'><br/>",
                        clientName, amount, imageCid);
            case PF:
                return String.format(
                        "Bonjour %s,<br/><br/>Votre paiement de facture de %s avec la référence %s a été effectué.<br/><br/><br/>Merci.<br/><img src='cid:%s' alt='Image'><br/>",
                        clientName, amount, reference, imageCid);
            default:
                return String.format(
                        "Bonjour %s,<br/><br/>Votre transaction a été traitée.<br/><br/>Merci.<br/><img src='cid:%s' alt='Image'><br/>",
                        clientName, imageCid);
        }
    }
}
