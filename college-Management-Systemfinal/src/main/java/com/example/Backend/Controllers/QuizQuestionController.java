package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.QuizQuestionRequest;
import com.example.Backend.Dto.Responses.QuizQuestionResponse;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Services.QuizQuestionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/quiz-question")
@RequiredArgsConstructor
@CrossOrigin("*")
public class QuizQuestionController {
    private final QuizQuestionService quizQuestionService;

    // Endpoint pour créer une nouvelle question de quiz
    @PostMapping("/create")
    public ResponseEntity<?> createQuizQuestion(@RequestBody QuizQuestionRequest request) {
        try {
            QuizQuestionResponse res = quizQuestionService.createQuizQuestion(request);
            return ResponseEntity.ok(res);
        } catch (IOException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la question du quiz : " + e.getMessage());
        }
    }

    // Endpoint pour mettre à jour une question de quiz existante
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateQuizQuestion(@RequestBody QuizQuestionRequest request, @PathVariable Long id) {
        try {
            QuizQuestionResponse res = quizQuestionService.updateQuizQuestion(request, id);
            return ResponseEntity.ok(res);
        } catch (NotFoundException | IOException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour de la question du quiz : " + e.getMessage());
        }
    }

    // Endpoint pour supprimer une question de quiz existante
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuizQuestion(@PathVariable Long id) {
        try {
            quizQuestionService.deleteQuizQuestion(id);
            return ResponseEntity.ok("La question du quiz a été supprimée avec succès");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression de la question du quiz : " + e.getMessage());
        }
    }

    // Endpoint pour récupérer toutes les questions d'un quiz spécifique
    @GetMapping("/all/{quizFormId}")
    public ResponseEntity<?> allQuizQuestions(@PathVariable Long quizFormId) {
        try {
            List<QuizQuestionResponse> res = quizQuestionService.allQuizQuestions(quizFormId);
            return ResponseEntity.ok().body(res);
        } catch (NotFoundException | IOException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des questions du quiz : " + e.getMessage());
        }
    }
    @PutMapping("/correct-proposition/{idQuestion}/{idProposition}")
    public ResponseEntity<?> correctProposition(@PathVariable Long idQuestion, @PathVariable Long idProposition) {
        try{
            System.out.println("hehehe");
            quizQuestionService.correctPoropsition(idQuestion,idProposition);
            return ResponseEntity.ok("la correcte proposition a ete modifier");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("question ou proposition 'existent pas");
        }
    }


    }
