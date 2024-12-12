package com.example.Backend.Dto.Responses;

import com.example.Backend.Entities.TypeQuiz;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
@Builder
public class SupportCoursResponse {
    private Long id;
   // private String type;
   // private MultipartFile image;
   private String image;
    private String description;
   private TypeQuiz typeQuizForm;
 //   private  String typeQuizForm;
    private  String typeSupportCours;
  // private  String courId;
    //private CoursResponse coursId;
    private EnseignantReponse enseignantId;
    private Date created_at;

}
