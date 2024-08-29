package com.adria.spring_oracle.repository;

import com.adria.spring_oracle.entities.TransactionStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionStatisticsRepository extends JpaRepository<TransactionStatistics, Long> {
}
