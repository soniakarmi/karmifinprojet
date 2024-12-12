package com.example.Backend.Dto.Responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtilisateurResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String address;
    private  String photo;
    private  String role;




}
