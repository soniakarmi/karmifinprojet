package com.example.Backend.Dto.Responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class QuizQuestionResponse {
    private Long id;
    private String title;
    private String description;
    private int points;
    private LocalDate created_at;
}
