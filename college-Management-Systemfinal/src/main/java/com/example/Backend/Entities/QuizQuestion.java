package com.example.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private int points;
    private Long responseCorrectId;

    @Temporal(TemporalType.DATE)
    private LocalDate created_at;

    @ManyToOne
    @JsonIgnoreProperties("quizQuestions")
    @JoinColumn(name = "quiz_form_id", referencedColumnName = "id")
    private QuizForm quizForm;


    @OneToMany(mappedBy = "quizQuestion", cascade = CascadeType.ALL)
    //@JsonIgnore
    @JsonIgnoreProperties("quizQuestion")

    private List<QuizProposition> quizPropositions;
}
