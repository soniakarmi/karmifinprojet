package com.example.Backend.Services;

import com.example.Backend.Dto.Responses.NoteResponse;
import com.example.Backend.Entities.Etudiant;
import com.example.Backend.Entities.Note;
import com.example.Backend.Entities.QuizForm;
import com.example.Backend.Entities.Utilisateur;
import com.example.Backend.Repositories.NoteRepository;
import com.example.Backend.Repositories.QuizFormRepository;
import com.example.Backend.Utility.GenralUtilities;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {


    private final NoteRepository noteRepository;
    private final QuizFormRepository quizFormRepository;

    // Liste des notes par QuizForm
    public List<NoteResponse> listeDesNotesParQuizForm(Long quizFormId) throws IOException {
        QuizForm quizForm = quizFormRepository.findById(quizFormId)
                .orElseThrow(() -> new EntityNotFoundException("QuizForm avec ID " + quizFormId + " introuvable"));

        List<Note> notes = noteRepository.findAllByQuizForm(quizForm);
        List<NoteResponse> noteResponses = new ArrayList<>();

        for (Note note : notes) {
            NoteResponse res = response(note);
            noteResponses.add(res);
        }

        return noteResponses;
    }

    // Liste des notes pour l'étudiant authentifié

    public List<NoteResponse> listeDesNotesParEtudiant() throws IOException {
        Utilisateur etudiant=GenralUtilities.getAuthenticatedUser();
        List<Note> notes=noteRepository.findAllByEtudiant((Etudiant) etudiant);
        List<NoteResponse> noteResponses=new ArrayList<>();
        for(Note note:notes){
            var res =response(note);
            noteResponses.add(res);
        }
        return noteResponses;


    }
    public NoteResponse response(Note note) throws IOException {
        var etudiant = note.getEtudiant();
        QuizForm quizForm = note.getQuizForm();

        if (quizForm == null) {
            throw new IllegalArgumentException("QuizForm is null for the given Note");
        }

        return NoteResponse.builder()
                .etudiant(UtilisateurService.response(etudiant))  // Assurez-vous que cette méthode est accessible
                .type(quizForm.getType().toString())

                .note(note.getNote())
                .build();
    }
}
