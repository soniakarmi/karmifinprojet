package com.example.Backend.Entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor

@DiscriminatorValue("PDF")
@ToString
public class PDF extends SupportCours {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    private TypeQuiz type ;
    //@Lob
   // private byte[] image;
    private  String image;


}
