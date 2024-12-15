package com.example.Backend.Dto.Responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ReclamationResponse {
    private Long id;
    private String sujet;
    private String description;
    private LocalDate date;
    private EtudiantResponse etudiant;
}
