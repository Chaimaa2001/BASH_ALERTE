package com.adria.spring_oracle.repository;

import com.adria.spring_oracle.dao.ParamSMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParamSMSRepository extends JpaRepository<ParamSMS,Long> {
}
