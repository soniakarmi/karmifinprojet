package com.example.Backend.Dto.Responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Data
public class CoursResponse {
    private Long id;
    private String titre;
    private String description;
    private LocalDate datedebut;
    private LocalDate datefin;
    private ClasseResponse classeId;
    private EnseignantReponse enseignantId;
   // private  Number classe;
    //private  Number enseignant;
}
