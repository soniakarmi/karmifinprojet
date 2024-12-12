package com.example.Backend.Dto.Responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NoteResponse {
    private EtudiantResponse etudiant;
    private String type;
    private String description;
    private String typeQuizForm;
    private float note;
}
