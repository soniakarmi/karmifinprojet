package com.example.Backend.Repositories;

import com.example.Backend.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {
    Optional<Utilisateur> findByEmail(String username);

    List<Utilisateur> findByRole(Role role);

    List<Etudiant> findByRoleAndAccepted(Role role, boolean b);

    List<Etudiant> findByClasse(Classe classe);


    Optional<Etudiant> findByCode(String code);
    Optional<Utilisateur>getUtilisateurById(Long id);





}
