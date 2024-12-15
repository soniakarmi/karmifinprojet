package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.QuizPropositionRequest;
import com.example.Backend.Dto.Responses.QuestionAndPreposition;
import com.example.Backend.Dto.Responses.QuizPropositionResponse;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Services.QuizPropositionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/quiz-proposition")
@RequiredArgsConstructor
@CrossOrigin("*")
public class QuizPropositionController {

    private final QuizPropositionService quizPropositionService;

    // Endpoint pour créer une nouvelle proposition de quiz
    @PostMapping("/create")
    public ResponseEntity<?> createQuizProposition(@RequestBody QuizPropositionRequest request) {
        try {
            QuizPropositionResponse res = quizPropositionService.createQuizProposition(request);
            return ResponseEntity.ok(res);
        } catch (IOException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la proposition du quiz : " + e.getMessage());
        }
    }

    // Endpoint pour mettre à jour une proposition de quiz existante
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateQuizProposition(@RequestBody QuizPropositionRequest request, @PathVariable Long id) {
        try {
            QuizPropositionResponse res = quizPropositionService.updateQuizProposition(request, id);
            return ResponseEntity.ok(res);
        } catch (NotFoundException | IOException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour de la proposition du quiz : " + e.getMessage());
        }
    }

    // Endpoint pour supprimer une proposition de quiz existante
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuizProposition(@PathVariable Long id) {
        try {
            quizPropositionService.deleteQuizProposition(id);
            return ResponseEntity.ok("La proposition du quiz a été supprimée avec succès");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression de la proposition du quiz : " + e.getMessage());
        }
    }

    // Endpoint pour récupérer toutes les propositions d'une question spécifique
    @GetMapping("/all/{questionId}")
    public ResponseEntity<?> allQuizPropositions(@PathVariable Long questionId) {
        try {
            QuestionAndPreposition res = quizPropositionService.allQuizPropositions(questionId);
            return ResponseEntity.ok().body(res);
        } catch (NotFoundException | IOException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des propositions du quiz : " + e.getMessage());
        }
    }
}
