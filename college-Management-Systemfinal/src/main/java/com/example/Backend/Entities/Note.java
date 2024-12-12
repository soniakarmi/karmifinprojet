package com.example.Backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "Quiz_Form", referencedColumnName = "id")
    private QuizForm quizForm;
    @ManyToOne
    @JoinColumn(name="etudiant" ,referencedColumnName = "id")
    private Etudiant etudiant;
    private float note;


}
