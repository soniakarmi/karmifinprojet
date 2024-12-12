package com.example.Backend.Services;


import com.example.Backend.Dto.Requests.QuizPropositionRequest;
import com.example.Backend.Dto.Responses.QuestionAndPreposition;
import com.example.Backend.Dto.Responses.QuizPropositionResponse;
import com.example.Backend.Dto.Responses.QuizQuestionResponse;
import com.example.Backend.Entities.*;
import com.example.Backend.Exceptions.NotFoundException;

import com.example.Backend.Repositories.QuizPropositionRepository;
import com.example.Backend.Repositories.QuizPropositionRepository;
import com.example.Backend.Repositories.QuizQuestionRepository;
import com.example.Backend.Repositories.SupportCoursRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizPropositionService {
    private final QuizPropositionRepository QuizPropositionRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizQuestionService quizQuestionService;
    public QuizPropositionResponse createQuizProposition(QuizPropositionRequest request) throws IOException {
        Optional<QuizQuestion> question=quizQuestionRepository.findById(request.getQuestionId());
        if(question .isPresent()) {
            QuizProposition quizProposition = QuizProposition.builder()
                    .created_at(LocalDate.now())
                    .description(request.getDescription())
                    .quizQuestion(question.get())
                    .build();
            quizProposition = QuizPropositionRepository.save(quizProposition);
            return response(quizProposition);

        }
        else{
            throw new EntityNotFoundException(" question form avec id "+ request.getQuestionId()+" n'existe pas ");
        }
    }

    public QuizPropositionResponse updateQuizProposition(QuizPropositionRequest request,Long QuizPropositionId) throws IOException {
        Optional<QuizProposition> QuizProposition =QuizPropositionRepository.findById(QuizPropositionId);
        if(QuizProposition.isPresent()){
            QuizProposition.get().setDescription(request.getDescription());
            var res=QuizPropositionRepository.save(QuizProposition.get());
            return response(res);

        }else {
            throw new NotFoundException("QuizProposition avec l'id "+QuizPropositionId+" n'existe pas");
        }
    }
    public void deleteQuizProposition(Long QuizPropositionId)  {
        Optional<QuizProposition> QuizProposition =QuizPropositionRepository.findById(QuizPropositionId);
        if(QuizProposition.isPresent()){
            QuizPropositionRepository.deleteById(QuizPropositionId);

        }else {
            throw new NotFoundException("QuizProposition avec l'id "+QuizPropositionId+" n'existe pas");
        }
    }
    public QuestionAndPreposition allQuizPropositions(Long questionId) throws IOException {
        Optional<QuizQuestion> question=quizQuestionRepository.findById(questionId);
        if(question.isPresent()){
            List<QuizProposition> QuizPropositions=QuizPropositionRepository.findAllByQuizQuestion(question.get());
            List<QuizPropositionResponse> QuizPropositionResponseList=new ArrayList<>();
            for(QuizProposition QuizProposition:QuizPropositions){
                var res=response(QuizProposition);
                QuizPropositionResponseList.add(res);
            }
            return QuestionAndPreposition(QuizPropositionResponseList,question.get());
        }
        else {
            throw new NotFoundException("question avec l'id "+questionId+" n'existe pas");

        }
    }
    public QuizPropositionResponse response(QuizProposition QuizProposition) throws IOException {

        return QuizPropositionResponse.builder()
                .id(QuizProposition.getId())
                .create_at(QuizProposition.getCreated_at())
                .description(QuizProposition.getDescription())
                .questionId(QuizProposition.getQuizQuestion().getId())
                .build();
    }
   public QuestionAndPreposition QuestionAndPreposition(List<QuizPropositionResponse> request , QuizQuestion question) throws IOException {
        return QuestionAndPreposition.builder()
                .quiz(request)
                .question(quizQuestionService.response(question))
                .build();

   }
}
