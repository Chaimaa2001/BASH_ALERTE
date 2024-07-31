package com.adria.spring_oracle.repository;

import com.adria.spring_oracle.dao.BankCode;
import com.adria.spring_oracle.dao.ParamSMS;
import com.adria.spring_oracle.dao.Transaction_Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParamSMSRepository extends JpaRepository<ParamSMS, Long> {
    // Méthode pour trouver les paramètres SMS en fonction du code de banque et du type de transaction
    List<ParamSMS> findByBankCodeAndTransactionType(BankCode bankCode, Transaction_Type transactionType);
}
