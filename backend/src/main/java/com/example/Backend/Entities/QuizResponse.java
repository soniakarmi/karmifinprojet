package com.example.Backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class QuizResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant", referencedColumnName = "id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "question", referencedColumnName = "id")
    private QuizQuestion question;

    @ManyToOne
    @JoinColumn(name = "proposition", referencedColumnName = "id")
    private QuizProposition quizProposition;
}