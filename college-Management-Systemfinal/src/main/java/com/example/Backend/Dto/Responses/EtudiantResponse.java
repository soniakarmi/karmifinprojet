package com.example.Backend.Dto.Responses;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EtudiantResponse {

    private String photo;
    private UtilisateurResponse etudiant;
    private ClasseResponse classe;
}
