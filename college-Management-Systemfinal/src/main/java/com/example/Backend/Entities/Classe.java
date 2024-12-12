package com.example.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    @OneToMany(mappedBy = "classe" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cours> coursList;
    @OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Etudiant> etudiants;
}
