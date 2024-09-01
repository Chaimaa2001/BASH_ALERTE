package com.adria.spring_oracle.controller;

import com.adria.spring_oracle.entities.TransactionStatistics;
import com.adria.spring_oracle.mappers.StatisticsService;
import com.adria.spring_oracle.repository.TransactionStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class TransactionStatisticsController {

    private final TransactionStatisticsRepository statisticsRepository;
    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<List<TransactionStatistics>> getAllStatistics() {
        List<TransactionStatistics> statisticsList = statisticsRepository.findAll();
        return ResponseEntity.ok(statisticsList);
    }

    @GetMapping("/statistics/{id}")
    public ResponseEntity<?> getStatisticsById(@PathVariable("id") Long id) {
        List<TransactionStatistics> statistics = statisticsService.getTransactionStatistics(id);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

}
