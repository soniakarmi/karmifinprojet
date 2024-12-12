package com.example.Backend.Services;

import com.example.Backend.Dto.Requests.ReclamationRequest;
import com.example.Backend.Dto.Responses.EtudiantResponse;
import com.example.Backend.Dto.Responses.ReclamationResponse;
import com.example.Backend.Dto.Responses.UtilisateurResponse;
import com.example.Backend.Entities.Etudiant;
import com.example.Backend.Entities.Reclamation;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Repositories.ReclamationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.Backend.Utility.GenralUtilities.getAuthenticatedUser;

@Service
@RequiredArgsConstructor
public class ReclamationService {
    private final ReclamationRepository reclamationRepository;
    private final JavaMailSender javaMailSender;

    public ReclamationResponse createReclamation(ReclamationRequest request) throws IOException {
        var author=(Etudiant) getAuthenticatedUser();
        Reclamation reclamation=Reclamation.builder()
                .date(LocalDate.now())
                .description(request.getDescription())
                .sujet(request.getSujet())
                .etudiant(author)
                .build();
        reclamation=reclamationRepository.save(reclamation);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo("sonia.karmi@esprit.tn");
            mimeMessageHelper.setSubject("Reclamation ");
            String emailBody = String.format("""
                    <p><b>Sujet</b>: %s</p>
                    <p><b>Description</b>:</p>
                    <p>%s</p>
                    <p><b>De</b>: %s (Email: %s, ID: %s)</p>
                """, request.getSujet(), request.getDescription(), author.getNom(), author.getEmail(), author.getId());

            mimeMessageHelper.setText(emailBody, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // Log the exception
            e.printStackTrace();
        }
        return response(reclamation);


    }
    public ReclamationResponse updateReclamation(ReclamationRequest request,Long reclamationId) throws IOException {
        var author=(Etudiant) getAuthenticatedUser();
        Optional<Reclamation> reclamation =reclamationRepository.findById(reclamationId);
        if(reclamation.isPresent()){
            reclamation.get().setSujet(request.getSujet());
            reclamation.get().setDescription(request.getDescription());
            var res=reclamationRepository.save(reclamation.get());
            return response(res);

        }else {
            throw new NotFoundException("reclamation avec l'id "+reclamationId+" n'existe pas");
        }
    }
    public void deleteReclamation(Long reclamationId)  {
        var author=(Etudiant) getAuthenticatedUser();
        Optional<Reclamation> reclamation =reclamationRepository.findByIdAndEtudiant(reclamationId,author);
        if(reclamation.isPresent()){
            reclamationRepository.deleteById(reclamationId);

        }else {
            throw new NotFoundException("reclamation avec l'id "+reclamationId+" n'existe pas");
        }
    }

    public List<ReclamationResponse> allReclamations() throws IOException {
        List<Reclamation> reclamations=reclamationRepository.findAll();
        List<ReclamationResponse> reclamationResponseList=new ArrayList<>();
        for(Reclamation reclamation:reclamations){
            var res=response(reclamation);
            reclamationResponseList.add(res);
        }
        return reclamationResponseList;
    }
    public List<ReclamationResponse> myReclamations() throws IOException {
        var author=(Etudiant) getAuthenticatedUser();
        List<Reclamation> reclamations=reclamationRepository.findByEtudiant(author);
        List<ReclamationResponse> reclamationResponseList=new ArrayList<>();
        for(Reclamation reclamation:reclamations){
            var res=response(reclamation);
            reclamationResponseList.add(res);
        }
        return reclamationResponseList;
    }

    public ReclamationResponse response(Reclamation reclamation) throws IOException {
        var author=reclamation.getEtudiant();
        return ReclamationResponse.builder()
                .id(reclamation.getId())
                .date(reclamation.getDate())
                .description(reclamation.getDescription())
                .sujet(reclamation.getSujet())
                .etudiant(EtudiantResponse.builder()

                        .etudiant(UtilisateurResponse.builder()
                                .nom(author.getNom())
                                .telephone(author.getTelephone())
                                .prenom(author.getPrenom())
                                .email(author.getEmail())
                                .address(author.getAdresse())
                                .role(author.getRole().toString())
                                .photo(author.getPhoto())
                                .id(author.getId())

                                .build())
                        .build())
                .build();
    }
}
