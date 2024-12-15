package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.AuthenticationRequest;
import com.example.Backend.Dto.Requests.RegisterRequest;
import com.example.Backend.Dto.Responses.AuthenticationResponse;
import com.example.Backend.Exceptions.InvalidPasswordException;
import com.example.Backend.Exceptions.UserAlreadyExistsException;
import com.example.Backend.Exceptions.UserNotFoundException;
import com.example.Backend.Services.AuthenticationService;
import com.example.Backend.Utils.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;


@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private  final StorageService storageService;
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam(value = "nom", required = true) String nom,
            @RequestParam(value = "prenom", required = true) String prenom,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "telephone", required = true) String telephone,
            @RequestParam(value = "adresse", required = true) String adresse,
            @RequestParam(value = "role", required = true) String role,
            @RequestParam(value="photo", required = true) MultipartFile photo // La photo est un MultipartFile ici
    ) throws Exception {
        try {

            RegisterRequest request = RegisterRequest.builder()
                    .nom(nom)
                    .prenom(prenom)
                    .password(password)
                    .email(email)
                    .telephone(telephone)
                    .adresse(adresse)
                    .role(role)
                    .build();

            return ResponseEntity.ok(authenticationService.register(request, photo));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.ok("L'utilisateur avec l'email " + email + " existe déjà.");
        }
    }


    @GetMapping("/verify")
    public ResponseEntity<?> confirmRegistration(@Valid @NotEmpty @RequestParam String token) {
        final String result = authenticationService.validateVerificationToken(token);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.login(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'authentification");
        }
    }

   @PutMapping("/mot-de-passe-oublier")
    public ResponseEntity<?> forgotPassword (@RequestParam String email){
        return new ResponseEntity<>(authenticationService.forgotPassword(email),HttpStatus.OK);
   }
    @PutMapping("/réinitialiser-mot-de-passe")
    public ResponseEntity<?> réinitialiser(@RequestParam String email,@RequestHeader String password){
        return new ResponseEntity<>(authenticationService.réinitialiser(email,password),HttpStatus.OK);
    }


    @GetMapping("/downloads/{fileName:.+}")
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



}
