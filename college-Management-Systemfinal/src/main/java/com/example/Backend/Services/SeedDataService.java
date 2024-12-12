package com.example.Backend.Services;

import com.example.Backend.Entities.Role;
import com.example.Backend.Entities.Utilisateur;
import com.example.Backend.Repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SeedDataService implements CommandLineRunner {

    private final UtilisateurRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("sonia@gmail.com").isEmpty()) {
            var newUser  = Utilisateur.builder()
                    .nom("sonia")
                    .prenom("sonia")
                    .email("sonia@gmail.com")
                    .enable(true)
                    .telephone("123456")
                    .adresse("addess")
                    .password(passwordEncoder.encode("123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(newUser);
        } else {
            System.out.println("");
        }

    }
}