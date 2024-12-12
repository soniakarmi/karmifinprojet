package com.example.Backend.Dto.Requests;

import lombok.Data;

@Data
public class QuizQuestionRequest {
    private Long quizFormId;
    private String title;
    private String description;
    private int points;
}
