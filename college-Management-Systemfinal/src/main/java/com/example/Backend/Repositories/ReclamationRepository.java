package com.example.Backend.Repositories;

import com.example.Backend.Entities.Etudiant;
import com.example.Backend.Entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation,Long> {

    List<Reclamation> findByEtudiant(Etudiant author);

    Optional<Reclamation> findByIdAndEtudiant(Long reclamationId, Etudiant author);
}
