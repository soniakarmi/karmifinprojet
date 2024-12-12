package com.example.Backend.Dto.Responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClasseResponse {
    private Long id;
    private String nom;

}
