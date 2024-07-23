package com.adria.spring_oracle.config;


import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.repository.BankAccountRepository;
import com.adria.spring_oracle.repository.BankTransactionRepository;
import com.adria.spring_oracle.service.EmailService;
import com.adria.spring_oracle.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class SpringBatchConfig {
    private final JobRepository jobRepository;
    private final BankTransactionRepository repository;
    private final PlatformTransactionManager platformTransactionManager;
    private final BankAccountRepository bankAccountRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    @Value("${inputFile}")
    private Resource inputResource;

    @Bean
    public Step importStep() {
        return new StepBuilder("csvImport", jobRepository)
                .<BankTransaction, BankTransaction>chunk(10, platformTransactionManager)
                .reader(fileItemReader())
                .processor(new BankTransactionProcessor(bankAccountRepository, emailService, smsService))
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
        lineTokenizer.setNames("id", "accountID", "strTransactionDate", "transactionType", "amount", "notificationMethod");
        lineMapper.setLineTokenizer(lineTokenizer);

        BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BankTransaction.class);

        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
}