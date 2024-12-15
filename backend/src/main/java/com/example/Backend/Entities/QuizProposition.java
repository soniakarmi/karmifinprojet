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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizProposition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;


    private boolean isCorrect=false;
    @Temporal(TemporalType.DATE)
    private LocalDate created_at;

    @ManyToOne
    @JsonIgnoreProperties("quizPropositions")
    @JoinColumn(name = "quizQuestion", referencedColumnName = "id")
    private QuizQuestion quizQuestion;

    @OneToMany(mappedBy ="quizProposition", cascade = CascadeType.ALL)
   // @JsonIgnore
    @JsonIgnoreProperties("quizProposition")


    private List<QuizResponse> quizResponses;
}