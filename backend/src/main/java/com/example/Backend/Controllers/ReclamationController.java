package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.ReclamationRequest;
import com.example.Backend.Dto.Responses.ReclamationResponse;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Services.ReclamationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reclamation")
@CrossOrigin("*")
public class ReclamationController {
    private final ReclamationService reclamationService;

    @PostMapping("/create")
    public ResponseEntity<?> createReclamation(@RequestBody ReclamationRequest request){
        try{
           ReclamationResponse res= reclamationService.createReclamation(request);
           return ResponseEntity.ok(res);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("erreur lors de la creation de la reclamation");
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReclamation(@RequestBody ReclamationRequest request,@PathVariable Long id){
        try{
            ReclamationResponse res= reclamationService.updateReclamation(request,id);
            return ResponseEntity.ok(res);
        } catch (NotFoundException | IOException e) {
            return ResponseEntity.badRequest().body("reclamation avec l'id "+id+" n'existe pas");
        }
    }
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<?> updateReclamation(@PathVariable Long id){
        try{
             reclamationService.deleteReclamation(id);
            return ResponseEntity.ok("la reclamation a été supprimer avec succes  ");
        } catch (NotFoundException  e) {
            return ResponseEntity.badRequest().body("reclamation avec l'id "+id+" n'existe pas");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> allReclamations(){
        try{
            List<ReclamationResponse> res=reclamationService.allReclamations();
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("")
    public ResponseEntity<?> myReclamations(){
        try{
            List<ReclamationResponse> res=reclamationService.myReclamations();
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
