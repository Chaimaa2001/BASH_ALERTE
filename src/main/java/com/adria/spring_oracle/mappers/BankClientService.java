package com.adria.spring_oracle.mappers;

import com.adria.spring_oracle.entities.BankClient;
import com.adria.spring_oracle.repository.BankClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BankClientService {

    private final BankClientRepository bankClientRepository;

    public Optional<BankClient> findById(Long id) {
        return bankClientRepository.findById(id);
    }
}
