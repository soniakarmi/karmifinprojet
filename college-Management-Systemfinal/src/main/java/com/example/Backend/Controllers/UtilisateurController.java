package com.example.Backend.Controllers;

import com.example.Backend.Dto.Responses.EnseignantReponse;
import com.example.Backend.Dto.Responses.EtudiantResponse;
import com.example.Backend.Dto.Responses.UtilisateurResponse;
import com.example.Backend.Entities.Role;
import com.example.Backend.Entities.Utilisateur;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/etudiant")
    public ResponseEntity<?> listEtudiants() {
        try {
            List<EtudiantResponse> res = utilisateurService.listEtudiants();
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//recuperer tout user
    @GetMapping("/all")
    public ResponseEntity<List<UtilisateurResponse>> getAllUtilisateur() {
        try {
            List<UtilisateurResponse> utilisateurs = utilisateurService.getAllUtilisateur();
            return ResponseEntity.ok(utilisateurs);
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


//recuperer tout role

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllUserRoles() {
        List<Role> userRoles = utilisateurService.getAllUserRoles();
        return new ResponseEntity<>(userRoles, HttpStatus.OK);
    }


    //update user
    @PutMapping("/update/{id}")

    public ResponseEntity<?> updateUtilisateur(@RequestBody Utilisateur utilisateurRequest, @PathVariable Long id) {
        try {
            UtilisateurResponse updatedUtilisateur = utilisateurService.updateUtilisateur(utilisateurRequest, id);
           return ResponseEntity.ok(updatedUtilisateur);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Utilisateur avec l'id " + id + " n'existe pas.");
        } catch (IOException e) {
           return ResponseEntity.status(500).body("Erreur lors de la mise à jour de l'utilisateur.");
        }
    }

    //  public ResponseEntity<?> updateUtilisateur(@RequestBody Utilisateur utilisateurRequest, @PathVariable Long id) {
    //    try {
    // Vérification de la présence du nom dans la requête
    //      if (utilisateurRequest.getNom() == null || utilisateurRequest.getNom().isEmpty()) {
    //        return ResponseEntity.badRequest().body("Le nom de l'utilisateur est requis.");
    //  }

    //UtilisateurResponse updatedUtilisateur = utilisateurService.updateUtilisateur(utilisateurRequest, id);
    // return ResponseEntity.ok(updatedUtilisateur);
    // } catch (NotFoundException e) {
    //   return ResponseEntity.badRequest().body("Utilisateur avec l'id " + id + " n'existe pas.");
    //} catch (IOException e) {
    //  return ResponseEntity.status(500).body("Erreur lors de la mise à jour de l'utilisateur.");
    // }
    // }




    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
            return ResponseEntity.ok("user a été supprimé avec succès.");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("User avec l'id " + id + " n'existe pas.");
        }
    }
    //get utilisateurbyId
    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<?> getUtilisateurById(@PathVariable Long id) {
        try {

            UtilisateurResponse utilisateurResponse = utilisateurService.response(utilisateurService.getUtilisateurById(id));
            return ResponseEntity.ok(utilisateurResponse);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé avec l'ID: " + id); // Retourne une erreur 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur"); // Retourne une erreur 500
        }
    }


    @GetMapping("/etudiant/not-accepted")
    public ResponseEntity<?> listEtudiantsnonacceptes(){
        try{
            List<EtudiantResponse> res=utilisateurService.listEtudiantsNonAccepter();
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/etudiant/accept/{id}/{idClasse}")
    public ResponseEntity<?> accepter(@PathVariable Long id, @PathVariable Long idClasse){
        try{
            utilisateurService.accepterEtudiant(id,idClasse);
            return ResponseEntity.ok("l'etudiant avec l'id "+id+" a ete accepter avec succes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/etudiant/classe/{classeId}")
    public ResponseEntity<?> listEtudiantparClasse(@PathVariable Long classeId){
        try{
            List<EtudiantResponse> res=utilisateurService.ListEtudiants(classeId);
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/enseignants")
    public ResponseEntity<?> listenseignant(){
        try{
            List<EnseignantReponse> res=utilisateurService.ListEnseignants();
            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}


