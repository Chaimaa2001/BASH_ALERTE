package com.adria.spring_oracle.repository;


import com.adria.spring_oracle.entities.BankClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankClientRepository extends JpaRepository<BankClient,Long> {


}
