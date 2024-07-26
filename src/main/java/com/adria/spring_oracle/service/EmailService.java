package com.adria.spring_oracle.service;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamEmail;
import com.adria.spring_oracle.parametrage.EmailStrategy;
import com.adria.spring_oracle.repository.ParamEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final JavaMailSender mailSender;
    private final Map<String, EmailStrategy> emailStrategies; // Injection des stratégies
    private final ParamEmailRepository repository;

    public void sendTransactionEmail(BankTransaction bankTransaction) {
        String transactionType = bankTransaction.getTransactionType().name();
        EmailStrategy strategy = emailStrategies.get(transactionType);

        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for transaction type: " + transactionType);
        }

        ParamEmail paramEmail = strategy.createParamEmail(bankTransaction);
        repository.save(paramEmail);
        String to = bankTransaction.getBankAccount().getBankClient().getEmail();
        String subject = paramEmail.getObject();
        String body = paramEmail.getCorps();

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
}
