package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.CoursRequest;
import com.example.Backend.Dto.Responses.CoursResponse;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Services.CoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cours")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CoursController {
    private  final CoursService coursService;
    @PostMapping("/create")
    public ResponseEntity<?> createCours(@RequestBody CoursRequest request){
        try{
            CoursResponse res= coursService.createCours(request);
            return ResponseEntity.ok(res);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("erreur lors de la creation de la Cours");
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCours(@RequestBody CoursRequest request,@PathVariable Long id){
        try{
            CoursResponse res= coursService.updateCours(request,id);
            return ResponseEntity.ok(res);
        } catch (NotFoundException | IOException e) {
            return ResponseEntity.badRequest().body("Cours avec l'id "+id+" n'existe pas");
        }
    }
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<?> updateCours(@PathVariable Long id){
        try{
            coursService.deleteCours(id);
            return ResponseEntity.ok("la Cours a été supprimer avec succes  ");
        } catch (NotFoundException  e) {
            return ResponseEntity.badRequest().body("Cours avec l'id "+id+" n'existe pas");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> allCourss(){
        try{
            List<CoursResponse> res=coursService.allCours();
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<?> coursParEnseignant(@PathVariable Long enseignantId){
        try{
            List<CoursResponse> res=coursService.coursParEnseignant(enseignantId);
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/classe/{classeId}")
    public ResponseEntity<?> coursParClasse(@PathVariable Long classeId){
        try{
            List<CoursResponse> res=coursService.coursParClasse(classeId);
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCourById(@PathVariable Long id) {
        try {
            CoursResponse cours = coursService.response(coursService.getCoursById(id));
            return ResponseEntity.ok(cours);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cours non trouvé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur");
        }
    }



}

