package com.example.Backend.Services;

import com.example.Backend.Dto.Requests.QuizQuestionRequest;
import com.example.Backend.Dto.Responses.QuizQuestionResponse;
import com.example.Backend.Entities.QuizForm;
import com.example.Backend.Entities.QuizProposition;
import com.example.Backend.Entities.QuizQuestion;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Repositories.QuizFormRepository;
import com.example.Backend.Repositories.QuizPropositionRepository;
import com.example.Backend.Repositories.QuizQuestionRepository;
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
public class QuizQuestionService {
    private final QuizQuestionRepository QuizQuestionRepository;
    private final QuizFormRepository quizFormRepository;
    private final QuizPropositionRepository quizPropositionRepository;
    public QuizQuestionResponse createQuizQuestion(QuizQuestionRequest request) throws IOException {
        // Récupérer le QuizForm à partir de son ID
        Optional<QuizForm> quizForm = quizFormRepository.findById(request.getQuizFormId());

        // Vérifier si le QuizForm existe
        if (quizForm.isPresent()) {
            // Créer la question de quiz en l'associant au QuizForm récupéré
            QuizQuestion quizQuestion = QuizQuestion.builder()
                    .created_at(LocalDate.now())
                    .description(request.getDescription())
                    .title(request.getTitle())
                    .quizForm(quizForm.get())  // Associer le QuizForm
                    .points(request.getPoints())
                    .build();

            // Sauvegarder la nouvelle question de quiz dans le repository
            quizQuestion =QuizQuestionRepository.save(quizQuestion);

            // Retourner la réponse contenant les détails de la question de quiz créée
            return response(quizQuestion);
        } else {
            // Lancer une exception si le QuizForm n'existe pas
            throw new EntityNotFoundException("QuizForm avec ID " + request.getQuizFormId() + " n'existe pas.");
        }
    }

    public QuizQuestionResponse updateQuizQuestion(QuizQuestionRequest request,Long quizQuestionId) throws IOException {
        Optional<QuizQuestion> quizQuestion =QuizQuestionRepository.findById(quizQuestionId);
        if(quizQuestion.isPresent()){
            quizQuestion.get().setDescription(request.getDescription());
            quizQuestion.get().setPoints(request.getPoints());
            quizQuestion.get().setTitle(request.getTitle());
            var res=QuizQuestionRepository.save(quizQuestion.get());
            return response(res);

        }else {
            throw new NotFoundException("QuizQuestion avec l'id "+quizQuestionId+" n'existe pas");
        }
    }
    public void deleteQuizQuestion(Long quizQuestionId)  {
        Optional<QuizQuestion> QuizQuestion =QuizQuestionRepository.findById(quizQuestionId);
        if(QuizQuestion.isPresent()){
            QuizQuestionRepository.deleteById(quizQuestionId);

        }else {
            throw new NotFoundException("QuizQuestion avec l'id "+quizQuestionId+" n'existe pas");
        }
    }
    public List<QuizQuestionResponse> allQuizQuestions(Long quizFormId) throws IOException {
        // Récupérer le QuizForm par son ID
        Optional<QuizForm> quizForm = quizFormRepository.findById(quizFormId);

        // Vérifier si le QuizForm existe
        if (quizForm.isPresent()) {
            // Récupérer toutes les questions associées à ce QuizForm
            List<QuizQuestion> quizQuestions = QuizQuestionRepository.findAllByQuizForm(quizForm.get());

            // Créer une liste pour stocker les réponses des questions
            List<QuizQuestionResponse> quizQuestionResponseList = new ArrayList<>();

            // Transformer chaque QuizQuestion en QuizQuestionResponse et l'ajouter à la liste
            for (QuizQuestion quizQuestion : quizQuestions) {
                var res = response(quizQuestion);
                quizQuestionResponseList.add(res);
            }

            // Retourner la liste des réponses
            return quizQuestionResponseList;
        } else {
            // Lancer une exception si le QuizForm n'existe pas
            throw new EntityNotFoundException("QuizForm avec l'ID " + quizFormId + " n'existe pas");
        }
    }

    public void correctPoropsition(Long idQuestion,Long idProposition){
        Optional<QuizQuestion> question= QuizQuestionRepository.findById(idQuestion);
        Optional<QuizProposition> proposition=quizPropositionRepository.findById(idProposition);
        if(question.isPresent() && proposition.isPresent()){
            question.get().setResponseCorrectId(idProposition);
           QuizQuestionRepository.save(question.get());
        }else {
            throw new NotFoundException("question ou proposition 'existent pas");
        }
    }
    public QuizQuestionResponse response(QuizQuestion quizQuestion) throws IOException {

        return QuizQuestionResponse.builder()
                .id(quizQuestion.getId())
                .created_at(quizQuestion.getCreated_at())
                .description(quizQuestion.getDescription())
                .points(quizQuestion.getPoints())
                .title(quizQuestion.getTitle())
                .build();
    }
}
