package com.example.Backend.Services;

import com.example.Backend.Dto.Responses.ClasseResponse;
import com.example.Backend.Dto.Responses.EnseignantReponse;
import com.example.Backend.Dto.Responses.EtudiantResponse;
import com.example.Backend.Dto.Responses.UtilisateurResponse;
import com.example.Backend.Entities.*;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Repositories.*;
import com.example.Backend.Utility.GenralUtilities;
import com.example.Backend.Utils.StorageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.Backend.Utility.PhotoUtility.retrievePhoto;

@Service
@RequiredArgsConstructor
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final StorageService storageService;
    private final ClasseRepository classeRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JavaMailSender javaMailSender;
    private final PaiementRepository paiementRepository;
    private final EnseignantRepository enseignantRepository;
    private final CoursRepository coursRepository;
    private final EtudiantRepository etudiantRepository;
    private final SupportCoursRepository supportCoursRepository;
  private final NotificationRepository notificationRepository;
  private final ReclamationRepository reclamationRepository;
  private final ParentRepository parentRepository;
    private static double montantTotale = 0;

    public List<EtudiantResponse> listEtudiants() throws IOException {
        List<Etudiant> etudiants = utilisateurRepository.findByRoleAndAccepted(Role.ETUDIANT, true);
        List<EtudiantResponse> etudiantResponseList = new ArrayList<>();
        for (Etudiant etudiant : etudiants) {
            var res = response(etudiant);
            etudiantResponseList.add(res);
        }
        return etudiantResponseList;
    }

    public List<EtudiantResponse> listEtudiantsNonAccepter() throws IOException {
        List<Etudiant> etudiants = utilisateurRepository.findByRoleAndAccepted(Role.ETUDIANT, false);
        List<EtudiantResponse> etudiantResponseList = new ArrayList<>();
        for (Etudiant etudiant : etudiants) {
            var res = response(etudiant);
            etudiantResponseList.add(res);
        }
        return etudiantResponseList;
    }


    // Méthode pour récupérer tous les utilisateurs
    public List<UtilisateurResponse> getAllUtilisateur() {
        try {
            List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
            return utilisateurs.stream()
                    .map(this::response)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des utilisateurs", e);
        }
}

    public UtilisateurResponse updateUtilisateur(Utilisateur utilisateur, Long utilisateurId) throws IOException {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(utilisateurId);

        if (utilisateurOpt.isPresent()) {
            Utilisateur existingUtilisateur = utilisateurOpt.get();
            existingUtilisateur.setNom(utilisateur.getNom());
            existingUtilisateur.setPrenom(utilisateur.getPrenom());
            existingUtilisateur.setEmail(utilisateur.getEmail());
            existingUtilisateur.setAdresse(utilisateur.getAdresse());
            existingUtilisateur.setTelephone(utilisateur.getTelephone());
            existingUtilisateur.setPhoto(utilisateur.getPhoto());
            Utilisateur updatedUtilisateur = utilisateurRepository.save(existingUtilisateur);

            return response(updatedUtilisateur);

        } else {

            throw new NotFoundException("Utilisateur avec l'ID " + utilisateurId + " n'existe pas");
        }
    }

    //get userbyid

    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
    }


    @Transactional
    public void deleteUtilisateur(Long id) {
        // Vérifier si l'utilisateur existe
        if (!utilisateurRepository.existsById(id)) {
            throw new NotFoundException("Utilisateur avec l'id " + id + " n'existe pas");
        }
        List<VerificationToken> tokens = verificationTokenRepository.findAll();
        if (!tokens.isEmpty()) {
          //  verificationTokenRepository.deleteAll(tokens);
            verificationTokenRepository.deleteById(id);
        }
        utilisateurRepository.deleteById(id);
    }



    public List<Role> getAllUserRoles() {
        return EnumSet.allOf(Role.class).stream()
                .filter(role -> role != Role.ADMIN)
                .collect(Collectors.toList());
    }

    public void accepterEtudiant(Long etudiantId, Long classId) throws IOException {
        Optional<Utilisateur> user = utilisateurRepository.findById(etudiantId);
        Optional<Classe> classe = classeRepository.findById(classId);

        if (user.isPresent() && classe.isPresent()) {
            var etudiant = (Etudiant) user.get();
            var cla = classe.get();
            Long min = 10000L;
            Long max = 99999L;
            Random random = new Random();
            long randomNumber = min + (long) (random.nextDouble() * (max - min));
            String code = "ETU" + Long.toString(randomNumber);

            etudiant.setAccepted(true);
            etudiant.setCode(code);
            etudiant.setClasse(cla);

            utilisateurRepository.save(etudiant);
            Paiement paiement=new Paiement();
            paiement.setEtudiant(etudiant);
            paiement.setStatus(StatutPaiement.INCOMPLETE);
            paiement.setNombreDeTranches(0);
            paiement.setMontantPaye(0.0);
            paiement.setMontantRestant(montantTotale);
            paiement.setMontantTotal(montantTotale);
            paiementRepository.save(paiement);

            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

                mimeMessageHelper.setTo(etudiant.getEmail());
                mimeMessageHelper.setSubject("Mail d'acceptation");
                String emailBody = String.format("""
                    <div>
                        <p>Félicitations ! Vous avez été accepté. Votre classe sera <b<%s</b> et votre code est <b>%s</b>.</p>
                    </div>
                """, cla.getNom(), etudiant.getCode());

                mimeMessageHelper.setText(emailBody, true);

                javaMailSender.send(mimeMessage);

                System.out.println("Etudiant accepté et email envoyé : " + etudiant.getEmail());
            } catch (MessagingException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    public void setMontantTotale(double montant) {
        montantTotale = montant;
    }

    public double getMontantTotale() {
        return montantTotale;
    }
    public List<EtudiantResponse> ListEtudiants(Long classeId) throws IOException {
        List<Etudiant> etudiants=utilisateurRepository.findByClasse(classeRepository.findById(classeId).get());
        List<EtudiantResponse> etudiantResponseList=new ArrayList<>();
        for(Etudiant etudiant:etudiants){
            var res=response(etudiant);
            etudiantResponseList.add(res);
        }
        return etudiantResponseList;
    }
    public List<EnseignantReponse> ListEnseignants() throws IOException {
        List<Utilisateur> enseignants=utilisateurRepository.findByRole(Role.ENSEIGNANT);
        List<   EnseignantReponse> enseignantResponseList=new ArrayList<>();
        for(Utilisateur enseignant:enseignants){
            var res=response((Enseignant) enseignant);
            enseignantResponseList.add(res);
        }
        return enseignantResponseList;
    }
    public String relateParentToEtudiant(String code){
        Utilisateur parent = GenralUtilities.getAuthenticatedUser();
        Optional<Etudiant> etudiant = utilisateurRepository.findByCode(code);
        if(etudiant.isPresent()){
            var par=(Parent) parent;
            etudiant.get().setParent(par);
            par.setEtudiant(etudiant.get());
            utilisateurRepository.save(par);
            utilisateurRepository.save(etudiant.get());
            return " le parent a ete relier a vec l'etudianr avec succes";
        }else {
            return "pas d'etudiant avec le code  "+code;
        }

    }
    public static EtudiantResponse response(Etudiant etudiant) throws IOException {
        ClasseResponse classe=null;
        if(etudiant.getAccepted()){
             classe=ClasseService.response(etudiant.getClasse());
        }
        return EtudiantResponse.builder()

                .photo( retrievePhoto(etudiant.getId()))
                .etudiant(UtilisateurResponse.builder()
                        .nom(etudiant.getNom())
                        .telephone(etudiant.getTelephone())
                        .prenom(etudiant.getPrenom())
                        .email(etudiant.getEmail())
                        .address(etudiant.getAdresse())
                        .id(etudiant.getId())

                        .build())
                .classe(classe)
                .build();
    }



    public UtilisateurResponse response(Utilisateur utilisateur) {
        return UtilisateurResponse.builder()
                .nom(utilisateur.getNom())
                .telephone(utilisateur.getTelephone())
                .prenom(utilisateur.getPrenom())
                .email(utilisateur.getEmail())
                .address(utilisateur.getAdresse())
                .id(utilisateur.getId())
                .photo(utilisateur.getPhoto())
                .role(utilisateur.getRole().toString())
                .build();
    }
    public static EnseignantReponse response(Enseignant enseignant) throws IOException {

        return EnseignantReponse.builder()

              //  .photo( retrievePhoto(enseignant.getId()))
              //  .enseignant(UtilisateurResponse.builder()
                        .nom(enseignant.getNom())
                        .telephone(enseignant.getTelephone())
                        .prenom(enseignant.getPrenom())
                        .email(enseignant.getEmail())
                        .address(enseignant.getAdresse())
                        .id(enseignant.getId())
                        .photo(retrievePhoto(enseignant.getId()))
                        .build();


    }







}


