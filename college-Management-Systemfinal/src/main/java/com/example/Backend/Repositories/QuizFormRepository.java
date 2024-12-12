package com.example.Backend.Repositories;

import com.example.Backend.Entities.QuizForm;
import com.example.Backend.Entities.TypeQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizFormRepository extends JpaRepository<QuizForm, Long> {
  //  @Query("SELECT q FROM QuizForm q WHERE q.cours.id = :coursId AND q.type = :typeQuiz")
    //List<QuizForm> findAllByCoursIdAndTypeQuiz(@Param("coursId") Long coursId, @Param("typeQuiz") TypeQuiz typeQuiz);

    @Query("SELECT q FROM QuizForm q WHERE q.supportCours.id = :supportcourId AND q.type = :typeQuiz")
    List<QuizForm> findAllBySupportcourIdAndTypeQuiz(@Param("supportcourId") Long supportcourId, @Param("typeQuiz") TypeQuiz typeQuiz);

}
