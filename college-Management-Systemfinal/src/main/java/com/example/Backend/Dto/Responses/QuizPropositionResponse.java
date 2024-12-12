package com.example.Backend.Dto.Responses;

import com.example.Backend.Entities.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
public class QuizPropositionResponse {
    private Long id;
    private String description;
    private LocalDate create_at;
    private Long questionId;

}
