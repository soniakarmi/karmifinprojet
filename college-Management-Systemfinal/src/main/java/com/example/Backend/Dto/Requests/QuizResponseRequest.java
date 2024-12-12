package com.example.Backend.Dto.Requests;

import lombok.Data;

@Data
public class QuizResponseRequest {
    private Long question;
    private Long propositionResponse;
}
