package com.adria.spring_oracle.config;


import com.adria.spring_oracle.entities.BankTransaction;
import com.adria.spring_oracle.repository.BankTransactionRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class BankTransactionItemWriter implements ItemWriter<BankTransaction> {
    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionItemWriter(BankTransactionRepository bankTransactionRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
    }



    @Override
    public void write(Chunk<? extends BankTransaction> chunk) throws Exception {
        bankTransactionRepository.saveAll(chunk);  }
}
