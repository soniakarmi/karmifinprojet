package com.example.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class QuizForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeQuiz type;
    private float coeif;
    @Temporal(TemporalType.DATE)
    private LocalDate lastUpdated_at;

    @OneToMany(mappedBy ="quizForm", cascade = CascadeType.ALL)
   // @JsonIgnore
    @JsonIgnoreProperties("quizForm")

    private List<QuizQuestion> quizQuestions;

    @OneToOne
    @JsonIgnore
    private SupportCours supportCours;



}