package com.example.Backend;

import com.example.Backend.Dto.Requests.ReclamationRequest;
import com.example.Backend.Dto.Responses.ReclamationResponse;
import com.example.Backend.Entities.Etudiant;
import com.example.Backend.Entities.Reclamation;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Repositories.ReclamationRepository;
import com.example.Backend.Services.ReclamationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReclamationServiceTest {

    @InjectMocks
    private ReclamationService reclamationService;

    @Mock
    private ReclamationRepository reclamationRepository;

    @Mock
    private JavaMailSender javaMailSender;

    private Etudiant etudiant;
    private ReclamationRequest reclamationRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks

        // Initialisation de l'étudiant avec toutes ses propriétés
        etudiant = new Etudiant();

        // Initialisation de la réclamation avec un sujet et une description
        reclamationRequest = new ReclamationRequest();
    }


    @Test
    void createReclamation_success() throws IOException, MessagingException {
        // Given
        Reclamation reclamation = new Reclamation(null, reclamationRequest.getSujet(), reclamationRequest.getDescription(), LocalDate.now(), etudiant);
        when(reclamationRepository.save(any(Reclamation.class))).thenReturn(reclamation);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        ReclamationResponse response = reclamationService.createReclamation(reclamationRequest);

        // Then
        assertNotNull(response);
        assertEquals(reclamationRequest.getSujet(), response.getSujet());
        assertEquals(reclamationRequest.getDescription(), response.getDescription());

        // Verifying that the send method is called once
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void updateReclamation_success() throws IOException {
        // Given
        Reclamation existingReclamation = new Reclamation(1L, "Old Subject", "Old Description", LocalDate.now(), etudiant);
        when(reclamationRepository.findById(1L)).thenReturn(Optional.of(existingReclamation));
        when(reclamationRepository.save(any(Reclamation.class))).thenReturn(existingReclamation);

        // When
        ReclamationResponse response = reclamationService.updateReclamation(reclamationRequest, 1L);

        // Then
        assertNotNull(response);
        assertEquals(reclamationRequest.getSujet(), response.getSujet());
        assertEquals(reclamationRequest.getDescription(), response.getDescription());
    }

    @Test
    void updateReclamation_notFound() {
        // Given
        when(reclamationRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                reclamationService.updateReclamation(reclamationRequest, 1L)
        );
        assertEquals("Reclamation with ID 1 does not exist", exception.getMessage()); // Improved error message
    }

    @Test
    void deleteReclamation_success() {
        // Given
        Reclamation existingReclamation = new Reclamation(1L, "Subject", "Description", LocalDate.now(), etudiant);
        when(reclamationRepository.findByIdAndEtudiant(1L, etudiant)).thenReturn(Optional.of(existingReclamation));

        // When
        reclamationService.deleteReclamation(1L);

        // Then
        verify(reclamationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteReclamation_notFound() {
        // Given
        when(reclamationRepository.findByIdAndEtudiant(1L, etudiant)).thenReturn(Optional.empty());

        // When / Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                reclamationService.deleteReclamation(1L)
        );
        assertEquals("Reclamation with ID 1 does not exist", exception.getMessage()); // Improved error message
    }

    @Test
    void allReclamations_success() throws IOException {
        // Given
        Reclamation reclamation = new Reclamation(1L, "Subject", "Description", LocalDate.now(), etudiant);
        when(reclamationRepository.findAll()).thenReturn(List.of(reclamation));

        // When
        var responses = reclamationService.allReclamations();

        // Then
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
    }

    @Test
    void myReclamations_success() throws IOException {
        // Given
        Reclamation reclamation = new Reclamation(1L, "Subject", "Description", LocalDate.now(), etudiant);
        when(reclamationRepository.findByEtudiant(etudiant)).thenReturn(List.of(reclamation));

        // When
        var responses = reclamationService.myReclamations();

        // Then
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
    }
}
