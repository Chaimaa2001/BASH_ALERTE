package com.adria.spring_oracle.repository;


import com.adria.spring_oracle.dao.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransaction,Long> {
}
