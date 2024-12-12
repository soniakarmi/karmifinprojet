package com.example.Backend.Repositories;

import com.example.Backend.Entities.QuizResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionResponseRepository extends JpaRepository<QuizResponse,Long> {
}
