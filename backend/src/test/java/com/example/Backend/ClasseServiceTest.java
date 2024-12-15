package com.example.Backend;

import com.example.Backend.Dto.Requests.ClasseRequest;
import com.example.Backend.Dto.Responses.ClasseResponse;
import com.example.Backend.Entities.Classe;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Repositories.ClasseRepository;
import com.example.Backend.Services.ClasseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClasseServiceTest {

    @InjectMocks
    private ClasseService classeService;

    @Mock
    private ClasseRepository classeRepository;

    private Classe classe;
    private ClasseRequest classeRequest;

    @BeforeEach
    void setUp() {
        // Initialisation de la classe pour les tests
        classe = Classe.builder()
                .id(1L)
                .nom("Classe Test")
                .build();

        classeRequest = new ClasseRequest();
        classeRequest.setNom("Classe Test");
    }

    @Test
    void testCreateClasse() throws IOException {
        // Arrange
        when(classeRepository.save(any(Classe.class))).thenReturn(classe);

        // Act
        ClasseResponse result = classeService.createClasse(classeRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Classe Test", result.getNom());
        verify(classeRepository, times(1)).save(any(Classe.class));
    }

    @Test
    void testUpdateClasse() throws IOException {
        // Arrange
        when(classeRepository.findById(1L)).thenReturn(Optional.of(classe));
        when(classeRepository.save(any(Classe.class))).thenReturn(classe);

        // Act
        ClasseResponse result = classeService.updateClasse(classeRequest, 1L);

        // Assert
        assertNotNull(result);
        assertEquals("Classe Test", result.getNom());
        verify(classeRepository, times(1)).save(any(Classe.class));
    }

    @Test
    void testUpdateClasse_NotFound() throws IOException {
        // Arrange
        when(classeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> classeService.updateClasse(classeRequest, 1L));
        verify(classeRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteClasse() {
        // Arrange
        when(classeRepository.findById(1L)).thenReturn(Optional.of(classe));
        doNothing().when(classeRepository).deleteById(1L);

        // Act
        classeService.deleteClasse(1L);

        // Assert
        verify(classeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteClasse_NotFound() {
        // Arrange
        when(classeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> classeService.deleteClasse(1L));
        verify(classeRepository, times(1)).findById(1L);
    }

    @Test
    void testAllClasses() throws IOException {
        // Arrange
        when(classeRepository.findAll()).thenReturn(List.of(classe));

        // Act
        var result = classeService.allClasses();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(classeRepository, times(1)).findAll();
    }
}

