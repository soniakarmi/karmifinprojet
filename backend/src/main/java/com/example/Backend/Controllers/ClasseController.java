package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.ClasseRequest;
import com.example.Backend.Dto.Responses.ClasseResponse;
import com.example.Backend.Dto.Responses.EtudiantResponse;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Services.ClasseService;
import com.example.Backend.Services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/classe")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ClasseController {
    private  final ClasseService classeService;
    private final UtilisateurService utilisateurService;
    @PostMapping("/create")
    public ResponseEntity<?> createClasse(@RequestBody ClasseRequest request){
        try{
            ClasseResponse res= classeService.createClasse(request);
            return ResponseEntity.ok(res);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("erreur lors de la creation de la Classe");
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClasse(@RequestBody ClasseRequest request,@PathVariable Long id){
        try{
            ClasseResponse res= classeService.updateClasse(request,id);
            return ResponseEntity.ok(res);
        } catch (NotFoundException | IOException e) {
            return ResponseEntity.badRequest().body("Classe avec l'id "+id+" n'existe pas");
        }
    }
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<?> updateClasse(@PathVariable Long id){
        try{
            classeService.deleteClasse(id);
            return ResponseEntity.ok("la Classe a été supprimer avec succes  ");
        } catch (NotFoundException  e) {
            return ResponseEntity.badRequest().body("Classe avec l'id "+id+" n'existe pas");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> allClasses(){
        try{
            List<ClasseResponse> res=classeService.allClasses();
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{idClasse}/etudiant")
    public ResponseEntity<?> etudiantParClasse(@PathVariable Long idClasse){
        try{
            List<EtudiantResponse> res=utilisateurService.ListEtudiants(idClasse);
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
