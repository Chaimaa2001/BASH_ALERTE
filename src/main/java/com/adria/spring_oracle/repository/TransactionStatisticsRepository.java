package com.adria.spring_oracle.repository;

import com.adria.spring_oracle.entities.TransactionStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionStatisticsRepository extends JpaRepository<TransactionStatistics, Long> {
    List<TransactionStatistics> findByBankTransactionId(Long transactionId);
}

