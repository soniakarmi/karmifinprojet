package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.QuizResponseRequest;
import com.example.Backend.Dto.Responses.QuizResult;
import com.example.Backend.Entities.*;
import com.example.Backend.Repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz-response")
@CrossOrigin("*")
public class QuizResponseController {
    private final UtilisateurRepository utilisateurRepository;
    private final SupportCoursRepository supportCoursRepository;
    private final QuizPropositionRepository quizPropositionRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuestionResponseRepository questionResponseRepository;
    private final NoteRepository noteRepository;
    private final QuizFormRepository quizFormRepository;
    private final QuizResponseRepository quizResponseRepository;



    @PostMapping("/submit/{quizFormId}")
    public ResponseEntity<?> submitQuiz(@PathVariable Long quizFormId, @RequestBody List<QuizResponseRequest> reponses) {
        // Récupérer le QuizForm correspondant à l'ID
        Optional<QuizForm> quizFormOpt = quizFormRepository.findById(quizFormId);
        if (quizFormOpt.isPresent()) {
            QuizForm quizForm = quizFormOpt.get();

            // Initialisation des variables pour le calcul des points et des réponses
            float totalPoints = 0;
            float earnedPoints = 0;
            int correctAnswers = 0;
            int incorrectAnswers = 0;
            List<QuizResult.QuizResponseDetail> responseDetails = new ArrayList<>();

            // Récupérer toutes les questions du quiz
            List<QuizQuestion> questions = quizQuestionRepository.findByQuizForm(quizForm);

            // Traitement des réponses de l'étudiant
            for (QuizQuestion question : questions) {
                // Rechercher si la question a une réponse soumise
                Optional<QuizResponseRequest> responseOpt = reponses.stream()
                        .filter(r -> r.getQuestion().equals(question.getId()))
                        .findFirst();

                if (responseOpt.isPresent()) {
                    // Récupérer la proposition associée à la réponse
                    QuizResponseRequest reponse = responseOpt.get();
                    QuizProposition proposition = quizPropositionRepository.findById(reponse.getPropositionResponse())
                            .orElseThrow(() -> new RuntimeException("Proposition introuvable pour l'ID " + reponse.getPropositionResponse()));

                    // Créer une nouvelle réponse pour l'étudiant
                    QuizResponse questionResponse = QuizResponse.builder()
                            .question(question)
                            .build();
                    questionResponseRepository.save(questionResponse);

                    boolean isCorrect = proposition.isCorrect();
                    // Mettre à jour les points en fonction de la vérification de la réponse
                    if (isCorrect) {
                        earnedPoints += question.getPoints();
                        correctAnswers++;
                    } else {
                        incorrectAnswers++;
                    }

                    // Ajouter les détails de la réponse
                    responseDetails.add(QuizResult.QuizResponseDetail.builder()
                            .questionId(question.getId())
                            .propositionDescription(proposition.getDescription())
                            .isCorrect(isCorrect)
                            .build());

                    totalPoints += question.getPoints();
                } else {
                    // Si la question n'a pas de réponse, la marquer comme incorrecte
                    responseDetails.add(QuizResult.QuizResponseDetail.builder()
                            .questionId(question.getId())
                            .propositionDescription("Pas de réponse")
                            .isCorrect(false) // Considérer comme incorrecte si aucune réponse n'est donnée
                            .build());
                    totalPoints += question.getPoints(); // Ajouter les points à la note totale
                    incorrectAnswers++; // Incrémenter le nombre de réponses incorrectes
                }
            }

            // Calcul du pourcentage
            float percentage = (earnedPoints / totalPoints) * 100;

            // Créer une nouvelle note pour l'étudiant
            Note note = Note.builder()
                    .note(earnedPoints)
                    .quizForm(quizForm)
                    .build();
            noteRepository.save(note);

            // Créer l'objet QuizResult avec les résultats
            QuizResult quizResult = QuizResult.builder()
                    .note(earnedPoints)
                    .phrase("Votre score est  : " + earnedPoints + " / " + totalPoints)
                    .percentage(percentage)
                    .correctAnswers(correctAnswers)
                    .incorrectAnswers(incorrectAnswers)
                    .details(responseDetails)
                    .build();

            // Retourner les résultats à l'étudiant
            return ResponseEntity.ok(quizResult);
        } else {
            return ResponseEntity.status(404).body("Quiz introuvable pour l'ID " + quizFormId);
        }
    }









  /*  @PostMapping("/submit/{quizFormId}")
    public ResponseEntity<?> submitQuiz(@PathVariable Long quizFormId, @RequestBody List<QuizResponseRequest> reponses) {
       *//* Utilisateur etudiantOpt = GenralUtilities.getAuthenticatedUser();

        if (!(etudiantOpt instanceof Etudiant)) {
            return ResponseEntity.status(403).body("L'utilisateur authentifié n'est pas un étudiant.");
        }*//*

        Optional<QuizForm> quizFormOpt = quizFormRepository.findById(quizFormId);
        if (quizFormOpt.isPresent()) {
          //  Etudiant etudiant = (Etudiant) etudiantOpt;
            QuizForm quizForm = quizFormOpt.get();
            float totalPoints = 0;
            float earnedPoints = 0;

            for (QuizResponseRequest reponse : reponses) {
                // Récupérer la proposition choisie
                QuizProposition proposition = quizPropositionRepository.findById(reponse.getPropositionResponse())
                        .orElseThrow(() -> new RuntimeException("Proposition introuvable pour l'ID " + reponse.getPropositionResponse()));

                // Récupérer la question associée
                QuizQuestion question = quizQuestionRepository.findById(reponse.getQuestion())
                        .orElseThrow(() -> new RuntimeException("Question introuvable pour l'ID " + reponse.getQuestion()));

                // Enregistrer la réponse de l'étudiant
                QuizResponse questionResponse = QuizResponse.builder()
                       // .etudiant(etudiant)
                        .question(question)
                        .build();
                questionResponseRepository.save(questionResponse);

                // Vérifier si la proposition est correcte
                if (proposition.isCorrect()) {
                    earnedPoints += question.getPoints();
                }

                // Ajouter les points totaux de la question
                totalPoints += question.getPoints();
            }

            // Enregistrer la note de l'étudiant
            Note note = Note.builder()
                    .note(earnedPoints)
                    .quizForm(quizForm)
                  //  .etudiant(etudiant)
                    .build();
            noteRepository.save(note);

            // Retourner le résultat du quiz
            QuizResult quizResult = QuizResult.builder()
                    .note(earnedPoints)
                    .phrase("Votre score est " + earnedPoints + " / " + totalPoints)
                    .build();

            return ResponseEntity.ok(quizResult);
        } else {
            return ResponseEntity.status(404).body("Quiz introuvable pour l'ID " + quizFormId);
        }
    }
*/

    /*@PostMapping("/create")
    public ResponseEntity<?> createRespense(@RequestBody QuizResponse request) {
        try {
           // QuizQuestion quizQuestion = quizQuestionRepository.findById(request.getQuestion().getId()).orElseThrow(() -> new RuntimeException("Quizquestion not found"));
            QuizProposition quizProposition = quizPropositionRepository.findById(request.getQuizProposition().getId()).orElseThrow(() -> new RuntimeException("quiz prop not found"));
            request.setQuizProposition(quizProposition);
            quizProposition.setCorrect(true);
            quizPropositionRepository.save(quizProposition);
            //request.setQuestion(quizQuestion);
            QuizResponse res = quizResponseRepository.save(request);
            return ResponseEntity.ok(res);
        } catch ( EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la question du quiz : " + e.getMessage());
        }
    }*/

    @PostMapping("/create")
    public ResponseEntity<?> createRespense(@RequestBody Map<String, Object> request) {
        try {
            // Récupérer l'ID de la proposition depuis la requête
            Long propositionId = Long.valueOf(request.get("proposition").toString());

            // Trouver la proposition dans la base de données
            QuizProposition quizProposition = quizPropositionRepository.findById(propositionId)
                    .orElseThrow(() -> new RuntimeException("Quiz proposition not found"));

            // Créer une nouvelle réponse
            QuizResponse quizResponse = new QuizResponse();
          ///  quizResponse.setQuizProposition(quizProposition);
            quizProposition.setCorrect(true);
            quizPropositionRepository.save(quizProposition);
            // Sauvegarder la réponse
            QuizResponse savedResponse = quizResponseRepository.save(quizResponse);

            return ResponseEntity.ok(savedResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la réponse : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur inattendue : " + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteResponse(@PathVariable Long id) {
        try {
            // Trouver la réponse dans la base de données
            QuizResponse quizResponse = quizResponseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Quiz response not found"));

            // Récupérer la proposition associée
            QuizProposition quizProposition = quizResponse.getQuizProposition();
            if (quizProposition != null) {
                // Réinitialiser la propriété 'correct' si nécessaire
                quizProposition.setCorrect(false);
                quizPropositionRepository.save(quizProposition);
            }

            // Supprimer la réponse
            quizResponseRepository.delete(quizResponse);

            // Renvoyer une réponse JSON en cas de succès
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Quiz response deleted successfully");
            response.put("id", id);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            // Renvoyer une erreur JSON en cas de problème
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Response not found");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            // Gérer les erreurs inattendues
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unexpected error");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}



                //  Optional<SupportCours> supportCoursOpt = supportCoursRepository.findById(quizFormId);
       // System.out.println("hello");
        //if ( supportCoursOpt.isPresent()) {
          //  Etudiant etudiant = (Etudiant) etudiantOpt;
            //QuizForm quizForm = (QuizForm) supportCoursOpt.get();

            //float totalPoints = 0;
           // float earnedPoints = 0;

        //    for (QuizResponseRequest reponse : reponses) {
          //      QuizProposition proposition = quizPropositionRepository.findById(reponse.getPropositionResponse())
            //            .orElseThrow(() -> new RuntimeException("Proposition introuvable"));
              //  QuizQuestion question = quizQuestionRepository.findById(reponse.getQuestion())
                //        .orElseThrow(() -> new RuntimeException("Question introuvable"));

                //QuizResponse questionResponse = QuizResponse.builder()
                  //      .etudiant(etudiant)
                    //    .QuizProposition(proposition)
                      //  .question(question)
                        //.build();

               // questionResponseRepository.save(questionResponse);

              //  if (proposition.getId().equals(question.getResponseCorrectId())) {
                 //   earnedPoints += question.getPoints();
                //}
                //totalPoints += question.getPoints();
          //  }
            //Note note = Note.builder()
              //      .note(earnedPoints)
                //    .quizForm(quizForm)
                  //  .etudiant(etudiant)
                    //.build();
           // noteRepository.save(note);

          //  QuizResult quizResult = QuizResult.builder()
            //        .note(earnedPoints)
              //      .phrase("Votre score est " + earnedPoints + " / " + totalPoints)
                //    .build();

           // return ResponseEntity.ok(quizResult);
       // } else {
         //   return ResponseEntity.status(404).body("Étudiant ou Quiz introuvable");
        //}
   // }
//}
