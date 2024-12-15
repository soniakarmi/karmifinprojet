package com.example.Backend.Dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class QuizPropositionRequest {
    private Long questionId;
    private String description;
}
