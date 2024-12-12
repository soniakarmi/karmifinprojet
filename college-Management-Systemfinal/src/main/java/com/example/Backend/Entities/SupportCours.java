package com.example.Backend.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SupportCours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Enumerated(EnumType.STRING)
    private TypeQuiz typeQuiz;




  private  String image; // Ce champ stockera le nom du fichier d'image

    private String description;

    private Date created_at;
    private  String typeSupportCours;

    @ManyToOne()
    @JoinColumn(name = "coursId", referencedColumnName = "id")
    private Cours cours;

    @ManyToOne()
    @JoinColumn(name = "enseignantId", referencedColumnName = "id")
    private Enseignant enseignant;

    @OneToOne(mappedBy = "supportCours", cascade = CascadeType.ALL)
    private QuizForm quizForm;
}
