package com.adria.spring_oracle.repository;

import com.adria.spring_oracle.entities.BankCode;
import com.adria.spring_oracle.entities.ParamEmail;
import com.adria.spring_oracle.entities.Transaction_Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParamEmailRepository extends JpaRepository<ParamEmail, Long> {
    List<ParamEmail> findByBankCodeAndTransactionType(BankCode bankCode, Transaction_Type transactionType);
}
