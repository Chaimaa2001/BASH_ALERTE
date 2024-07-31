package com.adria.spring_oracle.config;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.repository.BankTransactionRepository;
import com.adria.spring_oracle.repository.BankClientRepository; // Ajoutez ce dépôt
import com.adria.spring_oracle.service.EmailService;
import com.adria.spring_oracle.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
@Transactional
public class SpringBatchConfig {

    private final JobRepository jobRepository;
    private final BankTransactionRepository repository;
    private final BankClientRepository bankClientRepository; // Ajoutez ce dépôt
    private final PlatformTransactionManager platformTransactionManager;
    private final EmailService emailService;
    private final SmsService smsService;

    @Value("${inputFile}")
    private Resource inputResource;

    @Bean
    public Step importStep() {
        return new StepBuilder("csvImport", jobRepository)
                .<BankTransaction, BankTransaction>chunk(10, platformTransactionManager)
                .reader(fileItemReader())
                .processor(new BankTransactionProcessor(emailService, smsService, bankClientRepository)) // Passer le dépôt du client ici
                .writer(new BankTransactionItemWriter(repository))
                .build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importBanks", jobRepository)
                .start(importStep())
                .build();
    }

    @Bean
    public FlatFileItemReader<BankTransaction> fileItemReader() {
        FlatFileItemReader<BankTransaction> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setName("FFIR1");
        fileItemReader.setLinesToSkip(1);
        fileItemReader.setResource(inputResource);
        fileItemReader.setLineMapper(lineMapper());
        return fileItemReader;
    }

    @Bean
    public LineMapper<BankTransaction> lineMapper() {
        DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("transaction_id", "bank_client", "transaction_date", "transaction_type", "transaction_amount", "typeChequier", "referenceFacture", "notificationMethod");
        lineMapper.setLineTokenizer(lineTokenizer);

        CustomBankTransactionFieldSetMapper fieldSetMapper = new CustomBankTransactionFieldSetMapper();
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
}
