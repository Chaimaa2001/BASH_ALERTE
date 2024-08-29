package com.adria.spring_oracle.controller;

import com.adria.spring_oracle.entities.BankClient;
import com.adria.spring_oracle.mappers.BankClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
public class BankClientController {

    private final BankClientService bankClientService;

    @GetMapping("/{id}")
    public ResponseEntity<BankClient> getClientById(@PathVariable Long id) {
        Optional<BankClient> client = bankClientService.findById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/all")
    public List<BankClient> getAllClient() {
        List<BankClient> client = bankClientService.findByAll();
       return client;
    }
}
