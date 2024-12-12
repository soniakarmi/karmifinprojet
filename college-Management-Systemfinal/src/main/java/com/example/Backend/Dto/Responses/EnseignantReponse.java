package com.example.Backend.Dto.Responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EnseignantReponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String address;
   // private String specialite;
    private  String photo;
}
