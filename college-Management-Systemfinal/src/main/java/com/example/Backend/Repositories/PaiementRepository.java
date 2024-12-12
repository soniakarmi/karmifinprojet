package com.example.Backend.Repositories;

import com.example.Backend.Entities.Etudiant;
import com.example.Backend.Entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement,Long>{
    List<Paiement> findByEtudiant(Etudiant etudiant);
}
