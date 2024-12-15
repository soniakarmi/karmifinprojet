package com.example.Backend.Dto.Responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PaiementResponse {
    private List<PaiementDto> paiements;
}
