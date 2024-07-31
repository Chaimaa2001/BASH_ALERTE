package com.adria.spring_oracle.repository;


import com.adria.spring_oracle.dao.BankClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankClientRepository extends JpaRepository<BankClient,Long> {
    Optional<BankClient> findById(Long id);

}
