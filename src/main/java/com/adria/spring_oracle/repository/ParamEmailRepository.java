package com.adria.spring_oracle.repository;

import com.adria.spring_oracle.dao.ParamEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParamEmailRepository extends JpaRepository<ParamEmail,Long> {
}
