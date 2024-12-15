package com.example.Backend.Controllers;

import com.example.Backend.Dto.Responses.NoteResponse;
import com.example.Backend.Services.NoteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NoteController {
    private final NoteService noteService;

    // Récupérer les notes d'un QuizForm donné par son ID
    @GetMapping("/quiz-form/{quizFormId}")
    public ResponseEntity<List<NoteResponse>> getNotesByQuizForm(@PathVariable Long quizFormId) {
        try {
            List<NoteResponse> notes = noteService.listeDesNotesParQuizForm(quizFormId);
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/etudiant")
    public ResponseEntity<List<NoteResponse>> getNotesByEtudiant() {
        try {
            List<NoteResponse> notes = noteService.listeDesNotesParEtudiant();
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

