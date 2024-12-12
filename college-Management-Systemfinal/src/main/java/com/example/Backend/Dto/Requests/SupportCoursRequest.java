package com.example.Backend.Dto.Requests;

import com.example.Backend.Entities.TypeQuiz;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SupportCoursRequest {
    private Long coursId;
    private Long enseignantId;
    private Date created_at;
    private  TypeQuiz typeQuiz;

    private String image;
    private String description;
   private String typeSupportCours;


}
