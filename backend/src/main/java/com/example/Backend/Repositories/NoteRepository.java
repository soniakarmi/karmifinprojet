package com.example.Backend.Repositories;

import com.example.Backend.Entities.Etudiant;
import com.example.Backend.Entities.Note;
import com.example.Backend.Entities.QuizForm;
import com.example.Backend.Entities.SupportCours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> findAllByQuizForm(QuizForm supportCours);

    List<Note> findAllByEtudiant(Etudiant etudiant);
}
