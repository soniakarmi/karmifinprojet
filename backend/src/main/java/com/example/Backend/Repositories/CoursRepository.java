package com.example.Backend.Repositories;

import com.example.Backend.Entities.Classe;
import com.example.Backend.Entities.Cours;
import com.example.Backend.Entities.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursRepository extends JpaRepository<Cours,Long> {
    List<Cours> findAllByEnseignant(Enseignant enseignant);

    List<Cours> findAllByClasse(Classe classe);
    Optional<Cours> getCourById(Long id);
    Optional<Cours> findByEnseignantAndClasse(Enseignant enseignant, Classe classe);

}
