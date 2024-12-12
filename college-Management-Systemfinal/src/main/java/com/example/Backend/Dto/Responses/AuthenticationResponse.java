package com.example.Backend.Dto.Responses;

import com.example.Backend.Entities.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String address;
    private String photo;

    @Enumerated(EnumType.STRING)
    private Role role;




}
