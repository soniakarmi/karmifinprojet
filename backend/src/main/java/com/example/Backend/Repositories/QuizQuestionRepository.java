package com.example.Backend.Repositories;

import com.example.Backend.Entities.QuizForm;
import com.example.Backend.Entities.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion,Long> {
    List<QuizQuestion> findAllByQuizForm(QuizForm quizForm);
    List<QuizQuestion> findByQuizForm(QuizForm quizForm);
}
