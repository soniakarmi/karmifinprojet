package com.example.Backend.Dto.Requests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CoursRequest {
    private  Long id;
    private String titre;
    private String description;
    private LocalDate datedebut;
    private LocalDate datefin;
    private Long enseignantId;
    private Long classeId;
}
