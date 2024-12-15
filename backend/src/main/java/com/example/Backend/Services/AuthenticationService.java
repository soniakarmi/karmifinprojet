package com.example.Backend.Services;

import com.example.Backend.Config.Security.JwtService;
import com.example.Backend.Dto.Requests.AuthenticationRequest;
import com.example.Backend.Dto.Requests.RegisterRequest;
import com.example.Backend.Dto.Responses.AuthenticationResponse;
import com.example.Backend.Dto.Responses.Messgchekmail;
import com.example.Backend.Entities.*;
import com.example.Backend.Exceptions.InvalidPasswordException;
import com.example.Backend.Exceptions.UserAlreadyExistsException;
import com.example.Backend.Exceptions.UserNotFoundException;
import com.example.Backend.Repositories.UtilisateurRepository;
import com.example.Backend.Repositories.VerificationTokenRepository;
import com.example.Backend.Utils.StorageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.example.Backend.Utility.PhotoUtility.retrievePhoto;

@Service
@RequiredArgsConstructor
public class AuthenticationService  {


    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;
    private final StorageService storageService;
    @Transactional
    public Messgchekmail register(RegisterRequest request, MultipartFile file) throws Exception {
        // Vérifie si l'utilisateur existe déjà
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("L'utilisateur existe déjà");
        }

        Utilisateur user;
        switch (request.getRole().toUpperCase()) {
            case "ETUDIANT":
                user = new Etudiant();
                ((Etudiant) user).setAccepted(false);
                break;
            case "ENSEIGNANT":
                user = new Enseignant();
                break;
            case "ADMIN":
                user = new Administration();
                break;
            default:
                user = new Parent();
                break;
        }

        // Enregistrer la photo si elle est présente
        if (file != null && !file.isEmpty()) {
            String imageFileName = storageService.storeFile(file);
            request.setPhoto(imageFileName);
        }
        setUserDetails(user, request);
        return Messgchekmail.builder()
                .msg("Succès ! Veuillez vérifier votre e-mail pour terminer votre inscription")
                .build();
    }



    private void createVerificationTokenForUser(final Utilisateur user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    private void setUserDetails(Utilisateur user, RegisterRequest request) throws MessagingException {
        user.setAdresse(request.getAdresse());
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnable(false);
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        user.setTelephone(request.getTelephone());
        user.setPhoto(request.getPhoto());

        utilisateurRepository.save(user);
        final String token = UUID.randomUUID().toString();
        createVerificationTokenForUser(user, token);
        sendVerificationToken(token, user);
    }
    @Async
    public void sendVerificationToken(String token, Utilisateur user) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setTo(user.getEmail());
            final String confirmationUrl = "http://localhost:8080/" + "verify?token=" + token;
            mimeMessageHelper.setSubject("Mail de vérification");
            String emailBody = String.format("""
        <div>
          <p>Merci de créer un compte. Veuillez cliquer sur le lien ci-dessous pour activer votre compte. Ce lien expirera dans 24 heures.</p>
          <a href="%s">Activer mon compte</a>
        </div>
        """, confirmationUrl);

            mimeMessageHelper.setText(emailBody, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Log the exception
            e.printStackTrace();
        }
    }
    public AuthenticationResponse login(AuthenticationRequest request) throws IOException {
        var user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Validate the password using the encoder
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Mot de passe incorrect");
        }

        var jwtToken = jwtService.generateToken(user);
        return buildAuthenticationResponse(user, jwtToken);
    }

    private AuthenticationResponse buildAuthenticationResponse(Utilisateur user, String accessToken) throws IOException {
        AuthenticationResponse.AuthenticationResponseBuilder responseBuilder = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .address(user.getAdresse())
                .role(user.getRole());


        if (user.getRole() == Role.ETUDIANT ) {

            responseBuilder.photo(retrievePhoto(((Etudiant) user).getId()));
        }

        if (user.getRole() == Role.ENSEIGNANT ) {

            responseBuilder.photo(retrievePhoto(((Enseignant) user).getId()));
        }



        return responseBuilder.build();
    }


    public String  forgotPassword(String email) {
        var user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(" Réinitialisation de mot de passe");
            String emailBody = String.format("""
                    <div>
                    <p> Bonjour," %s+ ", </p>
                    <p><b>Vous avez récemment demandé à réinitialiser votre mot de passe,</b>
                    <p>Veuillez suivre le lien ci-dessous pour compléter l'action.</p>
                    <a href="http://localhost:8080/réinitialiser-mot-de-passe?email=%s" target="_blank">Cliquez ici pour définir un nouveau mot de passe  </a>    
                             </div>
                """, user.getPrenom(),email);

            mimeMessageHelper.setText(emailBody, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // Log the exception
            e.printStackTrace();
        }
        return "Veuillez vérifier votre messagerie électronique pour réinitialiser votre mot de passe.";
    }

    public String réinitialiser(String email, String password) {
        var user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        utilisateurRepository.save(user);
        return "mot de passe réinitialiser avec success ";
    }

    public String validateVerificationToken( String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken!=null){
           var utilisiateur= verificationToken.getUser();
           utilisiateur.setEnable(true);
           utilisateurRepository.save(utilisiateur);
           return "le compte a ete verifier avec succes";
        }else{
            return "erreur lors de la verification du compte";
        }

    }
}
