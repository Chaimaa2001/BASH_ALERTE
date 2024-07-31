package com.adria.spring_oracle.repository;

import com.adria.spring_oracle.dao.BankCode;
import com.adria.spring_oracle.dao.ParamEmail;
import com.adria.spring_oracle.dao.Transaction_Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParamEmailRepository extends JpaRepository<ParamEmail, Long> {
    List<ParamEmail> findByBankCodeAndTransactionType(BankCode bankCode, Transaction_Type transactionType);
}
