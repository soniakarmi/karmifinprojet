package com.example.Backend.Dto.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public class QuestionAndPreposition {
    @JsonProperty("question")
    private QuizQuestionResponse question;
    @JsonProperty("quiz")
    private List<QuizPropositionResponse> quiz;


}
