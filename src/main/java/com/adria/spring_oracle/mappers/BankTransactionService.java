package com.adria.spring_oracle.mappers;

import com.adria.spring_oracle.dto.BankTransactionDTO;
import com.adria.spring_oracle.entities.BankCode;
import com.adria.spring_oracle.entities.BankTransaction;
import com.adria.spring_oracle.entities.Transaction_Type;
import com.adria.spring_oracle.repository.BankTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankTransactionService {

    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionService(BankTransactionRepository bankTransactionRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
    }

    public List<BankTransactionDTO> findAll() {
        return bankTransactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BankTransactionDTO> findByBankCode(BankCode bankCode) {
        // Trouver les transactions par BankCode
        return bankTransactionRepository.findByBankClient_BankCode(bankCode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BankTransactionDTO convertToDTO(BankTransaction transaction) {
        BankTransactionDTO dto = new BankTransactionDTO();
        dto.setId(transaction.getId());
        dto.setTransactionDate(transaction.getTransactionDate());

        // Obtenez la description à partir du code
        String description = "N/A"; // Valeur par défaut
        if (transaction.getTransactionType() != null) {
            try {
                Transaction_Type type = Transaction_Type.fromCode(transaction.getTransactionType().getCode());
                description = type.getDescription();
            } catch (IllegalArgumentException e) {
                // Gérer le cas où le code de transaction est invalide
                description = "Inconnu";
            }
        }

        dto.setTransactionTypeDescription(description);
        dto.setAmount(transaction.getAmount());
        dto.setTypeChequier(transaction.getTypeChequier());
        dto.setReferenceFacture(transaction.getReferenceFacture());
        dto.setNotificationMethod(transaction.getNotificationMethod());
        dto.setBankClientID(transaction.getBankClient().getUserID());
        dto.setStatus(transaction.getNotificationStatus().toString());
        return dto;
    }
    public List<String> findDistinctTransactionTypes() {
        return bankTransactionRepository.findDistinctTransactionTypes();
    }
}
