package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.QuizFormRequest;
import com.example.Backend.Entities.QuizForm;
import com.example.Backend.Entities.TypeQuiz;
import com.example.Backend.Repositories.QuizFormRepository;
import com.example.Backend.Services.QuizFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/quizForms")
public class QuizFormController {
        @Autowired private QuizFormService quizFormService;
        @Autowired
        QuizFormRepository xx;

        //récupérer tous les QuizForms par supportcourId et typeQuiz
        @GetMapping("/bysupportcourAndTypeQuiz")
        public List<QuizForm> getQuizFormsBysupportcourIdAndTypeQuiz(
                @RequestParam Long supportcourId,
                @RequestParam TypeQuiz typeQuiz) {
                //return quizFormService.findAllByCoursIdAndTypeQuiz(coursId, typeQuiz);
                return quizFormService.findAllBysupportcourIdAndTypeQuiz(supportcourId, typeQuiz);
        }
        @GetMapping("/{id}")
        public QuizForm get(@PathVariable Long id ) {

                return xx.findById(id).get();
        }


        // Méthode pour créer un QuizForm
        @PostMapping("/create")
        public ResponseEntity<QuizForm> createQuizForm(@Valid @RequestBody  QuizFormRequest quizFormRequest) {
                QuizForm createdQuizForm = quizFormService.createQuizForm(quizFormRequest);
                return ResponseEntity.ok(createdQuizForm);
        }

        // Méthode pour supprimer un QuizForm
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteQuizForm(@PathVariable Long id) {
                quizFormService.deleteQuizForm(id);
                return ResponseEntity.noContent().build();
        }


}
