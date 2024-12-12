package com.example.Backend.Dto.Responses;

import com.example.Backend.Entities.StatutPaiement;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class PaiementDto {
    private long id;
    private LocalDate date;
    private Double montantTotal;
    private Double montantPaye;
    private Double montantRestant;
    private Integer nombreDeTranches;
    private Double montantParTranche;
    private StatutPaiement statut;
    private ClasseResponse classe;
}
