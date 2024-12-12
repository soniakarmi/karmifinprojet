package com.example.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data

@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Enseignant")
public class Enseignant extends Utilisateur{


    @OneToMany(mappedBy = "enseignant",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cours> coursList;
    @OneToMany(mappedBy = "enseignant",cascade = CascadeType.ALL)
    @JsonIgnore
    private  List<SupportCours>supportCourslist;
}
