package com.adria.spring_oracle.repository;


import com.adria.spring_oracle.entities.BankCode;
import com.adria.spring_oracle.entities.BankTransaction;
import com.adria.spring_oracle.entities.Transaction_Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransaction,Long> {
    List<BankTransaction> findByBankClient_BankCode(BankCode bankCode);
    @Query("SELECT DISTINCT t.transactionType FROM BankTransaction t")
    List<String> findDistinctTransactionTypeCodes();
    default List<String> findDistinctTransactionTypes() {
        return Arrays.stream(Transaction_Type.values())
                .map(Transaction_Type::getDescription)
                .collect(Collectors.toList());
    }
}
