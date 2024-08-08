package com.adria.spring_oracle.security;

import com.adria.spring_oracle.entities.BackOffice;
import com.adria.spring_oracle.repository.BackOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BackOfficeService {

    @Autowired
    private BackOfficeRepository backOfficeRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createUser(BackOffice user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        backOfficeRepository.save(user);
    }

    public boolean authenticate(String username, String password) {
        BackOffice user = backOfficeRepository.findByUsername(username);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
