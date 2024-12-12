package com.example.Backend.Dto.Requests;

import com.example.Backend.Entities.TypeQuiz;
import lombok.Data;

@Data

public class QuizFormRequest {
    private TypeQuiz type;

    private Long supportcoursId;
    private float coeif;
}
