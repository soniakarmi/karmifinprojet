package com.example.Backend.Repositories;

import com.example.Backend.Entities.Classe;
import com.example.Backend.Entities.QuizProposition;
import com.example.Backend.Entities.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizPropositionRepository extends JpaRepository<QuizProposition,Long> {
    List<QuizProposition> findAllByQuizQuestion(QuizQuestion quizQuestion);
}
