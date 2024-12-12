package com.example.Backend.Services;

import com.example.Backend.Dto.Requests.SupportCoursRequest;
import com.example.Backend.Dto.Responses.SupportCoursResponse;
import com.example.Backend.Entities.*;
import com.example.Backend.Repositories.CoursRepository;
import com.example.Backend.Repositories.EnseignantRepository;
import com.example.Backend.Repositories.SupportCoursRepository;
import com.example.Backend.Utils.StorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportCoursService {
    private final SupportCoursRepository supportCoursRepository;
    private final CoursRepository coursRepository;
    private  final StorageService storageService;
    private final EnseignantRepository enseignantRepository;
    private final UtilisateurService utilisateurService;



    public SupportCours createSupportCours(SupportCoursRequest supportCoursRequest, MultipartFile image) {
        SupportCours  supportCours = new SupportCours();
        //supportCours.setTypeQuiz(TypeQuiz.valueOf(supportCoursRequest.getTypeQuiz().name()));
        supportCours.setTypeQuiz(supportCoursRequest.getTypeQuiz());
        supportCours.setDescription(supportCoursRequest.getDescription());  //enum
        supportCours.setTypeSupportCours(supportCoursRequest.getTypeSupportCours());
        supportCours.setCreated_at(new Date());
        Cours cours = coursRepository.findById(supportCoursRequest.getCoursId()).orElseThrow(() -> new RuntimeException("Cours not found"));
        Enseignant enseignant = enseignantRepository.findById(supportCoursRequest.getEnseignantId()).orElseThrow(() -> new RuntimeException("Ens not found"));
        supportCours.setCours(cours);
        supportCours.setEnseignant(enseignant);
        if (image != null && !image.isEmpty()) {
            String imageFileName = storageService.storeFile(image);
            supportCours.setImage(imageFileName);
        }

        return supportCoursRepository.save(supportCours);
    }

    //Get Support Cour ById
    public SupportCours getSupportCoursById(Long id) {
        return supportCoursRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Support_Cours non trouvé avec l'ID: " + id));
    }

    //getAlltype quiz
    public List<TypeQuiz> getAlltypequiz() {
        return EnumSet.allOf(TypeQuiz.class).stream()
                //    .filter(role -> role != Role.ADMIN)
                .collect(Collectors.toList());
    }

//delete supporcour

    public void deleteSupportCours(Long supportCoursId) {
        if (!supportCoursRepository.existsById(supportCoursId)) {
            throw new EntityNotFoundException("Support Cours with ID " + supportCoursId + " does not exist");
        }
        supportCoursRepository.deleteById(supportCoursId);
    }

//update supportcour
    public SupportCoursResponse updateSupportCours(Long supportCoursId, SupportCoursRequest request) throws IOException {
        SupportCours supportCours = supportCoursRepository.findById(supportCoursId)
                .orElseThrow(() -> new EntityNotFoundException("Support Cours with ID " + supportCoursId + " does not exist"));

        if (supportCours instanceof PDF) {
            updatePdfSupportCours((PDF) supportCours, request);
        } else {
            throw new IllegalArgumentException("Unsupported Support Cours type");
        }

        return response(supportCoursRepository.save(supportCours));
    }



    private void updatePdfSupportCours(PDF pdf, SupportCoursRequest request) throws IOException {
        pdf.setDescription(request.getDescription());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            pdf.setImage(request.getImage());
        }

        pdf.setCreated_at(request.getCreated_at());
    }

//get allsupportcour
    public List<SupportCoursResponse> listSupportCours(Long coursId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new EntityNotFoundException("Cours with ID " + coursId + " does not exist"));

        return supportCoursRepository.findAllByCours(cours).stream()
                .map(this::response)
                .collect(Collectors.toList());
    }
    public List<SupportCoursResponse> listPdf(Long coursId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new EntityNotFoundException("Cours with ID " + coursId + " does not exist"));

        // Assurez-vous de spécifier le type PDF
        List<SupportCours> supports = supportCoursRepository.findAllByCoursAndTypeSupportCours(cours, TypeQuiz.DS);

        return supports.stream()
                .map(this::response)
                .collect(Collectors.toList());
    }

    public List<SupportCoursResponse> listQuizForm(Long coursId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new EntityNotFoundException("Cours with ID " + coursId + " does not exist"));

        return supportCoursRepository.findAllByCoursAndTypeSupportCours(cours, TypeQuiz.DS).stream()
                .map(this::response)
                .collect(Collectors.toList());
    }
    public PDF uploadPdf(Long pdfId) {
        SupportCours supportCours = supportCoursRepository.findById(pdfId)
                .orElseThrow(() -> new EntityNotFoundException("PDF with ID " + pdfId + " does not exist"));

        return (PDF) supportCours;
    }

    public SupportCoursResponse response(SupportCours supportCours) {
        if (supportCours instanceof PDF) {
            PDF pdf = (PDF) supportCours;
            return SupportCoursResponse.builder()
                    .id(pdf.getId())
                    .typeSupportCours(pdf.getTypeSupportCours())
                    .description(pdf.getDescription())
                    .image(pdf.getImage())
                    .created_at(pdf.getCreated_at())
                    .build();

        } else {
            throw new IllegalArgumentException("Unknown type of SupportCours");
        }
    }




    private PDF createPdfSupportCours(SupportCoursRequest request, Cours cours) throws IOException {
        PDF pdf = new PDF();
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            byte[] imageData = request.getImage().getBytes();
            pdf.setImage(request.getImage());
        }
        pdf.setDescription(request.getDescription());
        pdf.setCreated_at(new Date());
        pdf.setCours(cours);
        return pdf;
    }



}
