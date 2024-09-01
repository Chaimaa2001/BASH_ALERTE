package com.adria.spring_oracle.mappers;

import com.adria.spring_oracle.entities.TransactionStatistics;
import com.adria.spring_oracle.repository.TransactionStatisticsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticsService {

    private final TransactionStatisticsRepository statisticsRepository;

    public StatisticsService(TransactionStatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public List<TransactionStatistics> getTransactionStatistics(Long transactionId) {
        return (List<TransactionStatistics>) statisticsRepository.findByBankTransactionId(transactionId);
    }

}
