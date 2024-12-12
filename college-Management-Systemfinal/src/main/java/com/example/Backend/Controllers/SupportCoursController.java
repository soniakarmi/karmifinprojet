package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.SupportCoursRequest;
import com.example.Backend.Dto.Responses.SupportCoursResponse;
import com.example.Backend.Entities.PDF;
import com.example.Backend.Entities.SupportCours;
import com.example.Backend.Entities.TypeQuiz;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Services.SupportCoursService;
import com.example.Backend.Utils.StorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/support-cours")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SupportCoursController {
    private final SupportCoursService supportCoursService;
    private final StorageService storageService;

    @PostMapping("/creat")
    public ResponseEntity<SupportCours> createSupportCours(
            SupportCoursRequest supportCoursRequest,
            @RequestParam("file") MultipartFile file) {
        if (supportCoursRequest.getCoursId() == null) {
            throw new IllegalArgumentException("Cours ID cannot be null");
        }
        SupportCours supportCours = supportCoursService.createSupportCours(supportCoursRequest, file);
        return new ResponseEntity<>(supportCours, HttpStatus.CREATED);
    }


    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource file = storageService.loadFile(fileName);
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


    //Get Support Cour ById
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupportCourById(@PathVariable Long id) {
        try {
            SupportCoursResponse cours = supportCoursService.response(supportCoursService.getSupportCoursById(id));
            return ResponseEntity.ok(cours);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Support_Cours non trouvé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur");
        }
    }
    @GetMapping("/TypeQuiz")
    public ResponseEntity<List<TypeQuiz>> getAlltypequiz() {
        List<TypeQuiz> typequiz = supportCoursService.getAlltypequiz();
        return new ResponseEntity<>(typequiz, HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSupportCours(@PathVariable Long id,
                                                @RequestParam("typeSupportCours") String typeSupportCours,
                                                @RequestParam(value = "document", required = false) MultipartFile document,
                                                @RequestParam("description") String description,
                                                @RequestParam(value = "typeQuiz", required = false) TypeQuiz typeQuiz) {
        SupportCoursRequest supportCoursRequest = SupportCoursRequest.builder()
                //.pdf(ima)
                .typeSupportCours(typeSupportCours)
                //.typeQuizForm(typeQuiz)
                .description(description)
                .build();
        try {
            SupportCoursResponse response = supportCoursService.updateSupportCours(id, supportCoursRequest);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("support cours avec id " + id + " n'existe pas ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSupportCours(@PathVariable Long id) {
        try {
            supportCoursService.deleteSupportCours(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("support cours avec id " + id + " n'existe pas ");
        }
    }

    @GetMapping("/cours/{coursId}")
    public ResponseEntity<?> getSupportCoursByCoursId(@PathVariable Long coursId) {
        try {
            List<SupportCoursResponse> responseList = supportCoursService.listSupportCours(coursId);
            return ResponseEntity.ok(responseList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("cours avec id " + coursId + " n'existe pas ");
        }
    }

    @GetMapping("/pdf/cours/{coursId}")
    public ResponseEntity<?> getPdfByCoursId(@PathVariable Long coursId) {
        try {
            List<SupportCoursResponse> responseList = supportCoursService.listPdf(coursId);
            return ResponseEntity.ok(responseList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("cours avec id " + coursId + " n'existe pas ");
        }
    }

    @GetMapping("/quiz/cours/{coursId}")
    public ResponseEntity<?> getQuizByCoursId(@PathVariable Long coursId) {
        try {
            List<SupportCoursResponse> responseList = supportCoursService.listQuizForm(coursId);
            return ResponseEntity.ok(responseList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("cours avec id " + coursId + " n'existe pas ");
        }
    }



    @PostMapping("/create/{coursId}")
    public ResponseEntity<?> createSupportCours(@RequestParam("typeSupportCours") String typeSupportCours,
                                                @RequestParam(value="image", required = false) MultipartFile image,
                                                @RequestParam("description") String description,
                                                @RequestParam(value = "type", required = false) TypeQuiz typeQuizForm,
                                                @PathVariable Long coursId) {
        SupportCoursRequest supportCoursRequest = SupportCoursRequest.builder()
                //.image(image)
                .coursId(coursId)
              //  .typeQuizForm(typeQuizForm)
                .description(description)
                .typeSupportCours(typeSupportCours)
                .build();
        try {
            SupportCours response = supportCoursService.createSupportCours(supportCoursRequest,image);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(" cours avec id " + typeSupportCours + " n'existe pas ");
        }
    }



    @GetMapping("/pdf/{pdfId}")
    public ResponseEntity<?> getPdfById(@PathVariable Long pdfId) {
        try {
            PDF pdf = supportCoursService.uploadPdf(pdfId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "document.pdf" + "\"")  // Remplacer par un nom de fichier valide
                    .body(pdf.getImage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Erreur lors du téléchargement du document");
        }
    }


}
