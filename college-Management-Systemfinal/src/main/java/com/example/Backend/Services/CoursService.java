package com.example.Backend.Services;

import com.example.Backend.Dto.Requests.CoursRequest;
import com.example.Backend.Dto.Responses.CoursResponse;
import com.example.Backend.Entities.*;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Repositories.ClasseRepository;
import com.example.Backend.Repositories.CoursRepository;
import com.example.Backend.Repositories.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.Backend.Utility.GenralUtilities.getAuthenticatedUser;

@Service
@RequiredArgsConstructor
public class CoursService {
    private final CoursRepository coursRepository;
    private final ClasseRepository classeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ClasseService classeService;
    private final UtilisateurService utilisateurService;
    public CoursResponse createCours(CoursRequest request) throws IOException {
        Optional<Utilisateur>  enseignant=utilisateurRepository.findById(request.getEnseignantId());
        Optional<Classe>  classe=classeRepository.findById(request.getClasseId());
        if(enseignant.isPresent() && classe.isPresent()){
            Cours cours=Cours.builder()
                    .titre(request.getTitre())
                    .description(request.getDescription())
                    .datedebut(request.getDatedebut())
                    .datefin(request.getDatefin())
                    .enseignant((Enseignant) enseignant.get())
                    .classe(classe.get())
                    .build();
            cours=coursRepository.save(cours);
            return response(cours);
           // return  createCours(request);
        }else{
            throw  new EntityNotFoundException(" la classe ou l'enseignant est non trouvée");
        }


    }
    public CoursResponse updateCours(CoursRequest request,Long coursId) throws IOException {
        Optional<Cours> cours =coursRepository.findById(coursId);
        if(cours.isPresent()){
            Optional<Utilisateur>  enseignant=utilisateurRepository.findById(request.getEnseignantId());
            Optional<Classe>  classe=classeRepository.findById(request.getClasseId());
            var course=cours.get();
            if(enseignant.isPresent() && classe.isPresent()) {
                course.setDatedebut(request.getDatedebut());
                course.setDatefin(request.getDatefin());
                course.setDescription(request.getDescription());
                course.setTitre(request.getTitre());
                course.setClasse(classe.get());
                course.setEnseignant((Enseignant) enseignant.get());
                course = coursRepository.save(course);
                return response(course);

            }
            else {
                throw  new EntityNotFoundException(" la classe ou l'enseignant est non trouvée");
            }

        }else {
            throw new NotFoundException("Cours  avec l'id "+coursId+" n'existe pas");
        }
    }
    public void deleteCours(Long coursId)  {
        Optional<Cours> Cours =coursRepository.findById(coursId);
        if(Cours.isPresent()){
            coursRepository.deleteById(coursId);

        }else {
            throw new NotFoundException("cours avec l'id "+coursId+" n'existe pas");
        }
    }
    public List<CoursResponse> allCours() throws IOException {
        List<Cours> coursList=coursRepository.findAll();
        List<CoursResponse> CoursResponses=new ArrayList<>();
        for(Cours cours:coursList){
            var res=response(cours);
            CoursResponses.add(res);
        }
        return CoursResponses;
    }
    public List<CoursResponse> coursParEnseignant(Long enseignantId) throws IOException {
        Optional<Utilisateur> enseignant=utilisateurRepository.findById(enseignantId);
        if(enseignant.isPresent()){
            List<Cours> coursList=coursRepository.findAllByEnseignant((Enseignant) enseignant.get());
            List<CoursResponse> CoursResponses=new ArrayList<>();
            for(Cours cours:coursList){
                var res=response(cours);
                CoursResponses.add(res);
            }
            return CoursResponses;
        }
        else {
            throw new NotFoundException("enseignant avec l'id "+enseignantId+" n'existe pas");
        }

    }
    public List<CoursResponse> coursParClasse(Long classeId) throws IOException {
        Optional<Classe> classe=classeRepository.findById(classeId);
        if(classe.isPresent()){
            List<Cours> coursList=coursRepository.findAllByClasse(classe.get());
            List<CoursResponse> CoursResponses=new ArrayList<>();
            for(Cours cours:coursList){
                var res=response(cours);
                CoursResponses.add(res);
            }
            return CoursResponses;
        }
        else {
            throw new NotFoundException("classe avec l'id "+classeId+" n'existe pas");
        }

    }

    public CoursResponse response(Cours cours) throws IOException {
        return CoursResponse.builder()
                .id(cours.getId())
                .titre(cours.getTitre())
                .datefin(cours.getDatefin())
                .datedebut(cours.getDatedebut())
                .description(cours.getDescription())
                .classeId(classeService.response(cours.getClasse()))
                .enseignantId(utilisateurService.response(cours.getEnseignant()))
                .build();
    }

    public CoursResponse getCoursByEnseignantAndClasse(Long enseignantId, Long classeId) throws IOException {
        Utilisateur enseignant = utilisateurRepository.findById(enseignantId)
                .orElseThrow(() -> new EntityNotFoundException("Enseignant avec l'ID " + enseignantId + " n'existe pas."));
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new EntityNotFoundException("Classe avec l'ID " + classeId + " n'existe pas."));

        Cours cours = coursRepository.findByEnseignantAndClasse((Enseignant) enseignant, classe)
                .orElseThrow(() -> new NotFoundException("Aucun cours trouvé pour l'enseignant avec l'ID " + enseignantId + " et la classe avec l'ID " + classeId));

        return response(cours);
    }

//Get  Cour ById
public Cours getCoursById(Long id) {
    return coursRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cours non trouvé avec l'ID: " + id));
}


}
