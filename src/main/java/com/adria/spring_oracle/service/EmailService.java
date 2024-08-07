package com.adria.spring_oracle.service;

import com.adria.spring_oracle.entities.BankTransaction;
import com.adria.spring_oracle.entities.ParamEmail;
import com.adria.spring_oracle.entities.BankCode;
import com.adria.spring_oracle.entities.Transaction_Type;
import com.adria.spring_oracle.repository.ParamEmailRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Service
@Transactional
public class EmailService {

    private final JavaMailSender mailSender;
    private final ParamEmailRepository repository;

    public EmailService(JavaMailSender mailSender, ParamEmailRepository repository) {
        this.mailSender = mailSender;
        this.repository = repository;
    }

    public void sendTransactionEmail(BankTransaction bankTransaction) {
        // Récupérer le ParamEmail correspondant au BankCode et au type de transaction
        BankCode bankCode = bankTransaction.getBankClient().getBankCode();
        Transaction_Type transactionType = bankTransaction.getTransactionType();

        List<ParamEmail> paramEmails = repository.findByBankCodeAndTransactionType(bankCode, transactionType);
        if (paramEmails.isEmpty()) {
            throw new IllegalArgumentException("No email parameters found for bank code: " + bankCode +
                    " and transaction type: " + transactionType);
        }

        // Vous pouvez choisir ici comment gérer plusieurs ParamEmail (par exemple, utiliser le premier)
        ParamEmail paramEmail = paramEmails.get(0); // Choisir le premier ou adapter selon votre logique

        // Préparer les valeurs à remplacer dans le corps de l'email
        String prenom = bankTransaction.getBankClient().getPrenom();
        String nom = bankTransaction.getBankClient().getNom();
        String transactionTypeName = transactionType.name();  // Nom de la transaction
        String montant = bankTransaction.getAmount() != null ? bankTransaction.getAmount().toString() : "N/A";
        String typeChequier = bankTransaction.getTypeChequier() != null ? bankTransaction.getTypeChequier() : "N/A";
        String referenceFacture = bankTransaction.getReferenceFacture() != null ? bankTransaction.getReferenceFacture() : "N/A";

        // Remplacer les placeholders dans le corps de l'email
        String body = paramEmail.getCorps()
                .replace("{prenom}", prenom != null ? prenom : "N/A")
                .replace("{nom}", nom != null ? nom : "N/A")
                .replace("{transactionType}", transactionTypeName != null ? transactionTypeName : "N/A")
                .replace("{bankCode}", bankCode.name() != null ? bankCode.name() : "N/A")
                .replace("{montant}", montant)
                .replace("{typeChequier}", typeChequier)
                .replace("{referenceFacture}", referenceFacture);

        String to = bankTransaction.getBankClient().getEmail();
        String subject = paramEmail.getObject();
        String from = paramEmail.getEmetteur();

        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Client email is not provided");
        }

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);  // 'true' indique du contenu HTML
            helper.setFrom(from);

            // Choisir l'image en fonction du BankCode
            String imageFilePath = getImageFilePath(bankCode);
            File imageFile = new File(imageFilePath);
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

    private String getImageFilePath(BankCode bankCode) {
        switch (bankCode) {
            case TIJJARI:
                return "src/main/resources/static/attijari.png";
            case BMCE:
                return "src/main/resources/static/bmce.png";
            case CIH:
                return "src/main/resources/static/cih.png";
            case CREDIT_DU_MAROC:
                return "src/main/resources/static/cdm.png";
            default:
                throw new IllegalArgumentException("Unknown BankCode: " + bankCode);
        }
    }
}
