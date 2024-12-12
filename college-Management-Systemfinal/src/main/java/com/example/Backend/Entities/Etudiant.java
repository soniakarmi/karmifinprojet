package com.example.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Etudiant")
public class Etudiant extends Utilisateur {
    @Column(unique = true)
    private String code;
    private Boolean accepted;


    @OneToMany(mappedBy = "etudiant",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Reclamation> reclamations;

    @OneToMany(mappedBy = "etudiant",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Notification> notifications;

    @ManyToOne
    @JoinColumn(name = "classe", referencedColumnName = "id")
    private Classe classe;

    @OneToMany(mappedBy = "etudiant",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Note> notes;

    @ManyToOne
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private Parent parent;

    @OneToMany(mappedBy = "etudiant",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Paiement> paiements;

}


