package com.adria.spring_oracle.repository;

import com.adria.spring_oracle.entities.BackOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackOfficeRepository extends JpaRepository<BackOffice, Long> {
    BackOffice findByUsername(String username);
}
