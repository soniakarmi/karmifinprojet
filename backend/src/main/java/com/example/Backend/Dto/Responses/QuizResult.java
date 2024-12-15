package com.example.Backend.Dto.Responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizResult {
    private float note;
    private String phrase;
    private float percentage; // Nouveau : Pourcentage de réussite
    private int correctAnswers; // Nouveau : Nombre de réponses correctes
    private int incorrectAnswers; // Nouveau : Nombre de réponses incorrectes
    private List<QuizResponseDetail> details; // Nouveau : Détails des réponses

    @Data
    @Builder
    public static class QuizResponseDetail {  // Classe interne statique
        private Long questionId;
        private String propositionDescription;
        private boolean isCorrect;
    }
}


