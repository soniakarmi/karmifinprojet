package com.example.Backend.Services;

import com.example.Backend.Dto.Responses.PaiementDto;
import com.example.Backend.Dto.Responses.PaiementResponse;
import com.example.Backend.Entities.*;
import com.example.Backend.Repositories.PaiementRepository;
import com.example.Backend.Repositories.UtilisateurRepository;
import com.example.Backend.Utility.GenralUtilities;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaiementService {
    private final PaiementRepository paiementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final RestTemplate restTemplate;
    // private final UtilisateurService utilisateurService;

    public PaiementService(PaiementRepository paiementRepository, UtilisateurRepository utilisateurRepository , RestTemplate restTemplate) {
        this.paiementRepository = paiementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.restTemplate = restTemplate;
        // this.utilisateurService = utilisateurService;
    }

    public String generatePaymentLink(Long etudiantId, Double montant) throws Exception {
        String url = "https://api.preprod.konnect.network/api/v2/payments/init-payment";

        // Set up the headers, including the required x-api-key
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-api-key", "671a8e1005b326a4004ffe46:qJCjI3c93c4F9zvRBMgItpk");  // Replace with your actual API key

        // Prepare the payload (the JSON body to send)
        Map<String, Object> body = new HashMap<>();
        body.put("receiverWalletId", "671a8e1005b326a4004ffe4e");
        body.put("token", "TND");
        body.put("amount", montant);
        body.put("type", "immediate");
        body.put("description", "Payment for student ID: " + etudiantId);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Make the HTTP call
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // Assuming the API returns the payment URL and reference
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("payUrl") && responseBody.containsKey("paymentRef")) {
                String payUrl = responseBody.get("payUrl").toString();
                String paymentRef = responseBody.get("paymentRef").toString();

                // Return both the payment link and reference
                return "Payment link generated: " + payUrl + " | Payment Reference: " + paymentRef;
            }else {
                throw new Exception("Failed to generate payment link. No 'payUrl' returned.");
            }
        } else {
            throw new Exception("Error while calling the payment API: " + response.getStatusCode());
        }
    }



    public Map<String, Object> checkPaymentStatus(String paymentId) {
        String url = "https://api.preprod.konnect.network/api/v2/payments/" + paymentId; // Adjust URL as needed

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            // Check if the response is OK and the body is not null
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> paymentData = (Map<String, Object>) response.getBody().get("payment");

                // Extract the required fields
                Map<String, Object> result = new HashMap<>();
                result.put("amount", paymentData.get("amount"));
                result.put("token", paymentData.get("token"));
                result.put("status", paymentData.get("status"));
                result.put("orderId", paymentData.get("orderId"));

                return result; // Return the extracted fields as a Map
            } else {
                // Log the response status and body for debugging
                System.err.println("Error: " + response.getStatusCode() + " - " + response.getBody());
                throw new Exception("Unable to check payment status. HTTP Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            // Log the exception message for debugging
            System.err.println("Exception occurred: " + e.getMessage());
            return Collections.singletonMap("error", "Error occurred while checking payment status: " + e.getMessage());
        }
    }







    @Transactional
    public String payer(Long etudiantId, Double montant) throws Exception {
        // Find the student by ID
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(etudiantId);
        if (utilisateur.isEmpty()) {
            throw new Exception("Pas d'utilisateur avec l'id " + etudiantId);
        }

        // Check if the found user is an Etudiant
        if (!(utilisateur.get() instanceof Etudiant)) {
            throw new Exception("L'utilisateur avec l'id " + etudiantId + " n'est pas un étudiant.");
        }

        // Cast to Etudiant
        Etudiant etudiant = (Etudiant) utilisateur.get();

        // Retrieve existing payments for the student
        List<Paiement> paiements = paiementRepository.findByEtudiant(etudiant);
        Double montantPayeTotal = paiements.stream().mapToDouble(Paiement::getMontantPaye).sum();

        // Determine the new total amount to pay
        Double montantTotal = 10000.0; // Fixed total amount for the course/semester
        montantPayeTotal += montant; // Add new payment to the total paid

        // Debugging logs
        System.out.println("Etudiant ID: " + etudiantId);
        System.out.println("Montant Total: " + montantTotal);
        System.out.println("Montant Payé Total: " + montantPayeTotal);
        System.out.println("Montant Restant: " + (montantTotal - montantPayeTotal));

        // Create new Paiement entry for the student
        Paiement paiement = new Paiement();
        paiement.setEtudiant(etudiant);
        paiement.setDate(LocalDateTime.now());
        paiement.setMontantTotal(montantTotal);
        paiement.setMontantPaye(montant);
        paiement.setMontantRestant(montantTotal - montantPayeTotal);
        paiement.setStatus(paiement.getMontantRestant() <= 0 ? StatutPaiement.COMPLET : StatutPaiement.ACCEPTE);

        // Save the new payment
        paiementRepository.save(paiement);

        return "Le paiement de " + montant + " pour l'étudiant a été effectué avec succès.";
    }


    public PaiementResponse paiement() throws Exception, IOException {
        Utilisateur utilisateur = GenralUtilities.getAuthenticatedUser();
        Etudiant etudiant;
        if (utilisateur.getRole() == Role.ETUDIANT) {
            etudiant = (Etudiant) utilisateur;
        } else {
            Parent parent = (Parent) utilisateur;
            etudiant = parent.getEtudiant();
        }

        // Retrieve the list of payments
        List<Paiement> paiements = paiementRepository.findByEtudiant(etudiant);
        if (!paiements.isEmpty()) {
            // Prepare a response with all payments related to the student
            List<PaiementDto> paiementDtos = paiements.stream().map(p -> {
                try {
                    return PaiementDto.builder()
                            .id(p.getId())
                            .date(p.getDate().toLocalDate())
                            .montantTotal(p.getMontantTotal())
                            .montantPaye(p.getMontantPaye())
                            .montantRestant(p.getMontantRestant())
                            .nombreDeTranches(p.getNombreDeTranches())
                            .montantParTranche(p.getMontantParTranche())
                            .classe(ClasseService.response(etudiant.getClasse())) // This can throw IOException
                            .statut(p.getStatus())
                            .build();
                } catch (IOException | java.io.IOException e) {
                    // Handle IOException if needed
                    throw new RuntimeException("Erreur lors de la récupération de la classe", e);
                }
            }).toList();

            return PaiementResponse.builder()

                    .paiements(paiementDtos) // Pass the list of payment DTOs here
                    .build();
        } else {
            throw new Exception("Aucun paiement trouvé pour l'étudiant");
        }
    }




    // public void setNombreTranche(int nombre) {
    //     Utilisateur etudiant = GenralUtilities.getAuthenticatedUser();
    //     Optional<Paiement> paiement = paiementRepository.findByEtudiant((Etudiant) etudiant);
    //     if (paiement.isPresent()) {
    //         Paiement existingPaiement = paiement.get();
    //         existingPaiement.setNombreDeTranches(nombre);
    //         existingPaiement.setMontantParTranche(existingPaiement.getMontantTotal() / nombre);
    //         paiementRepository.save(existingPaiement);
    //     }

    // }

    // public void setMontantTotale(Double montant) {
    //     utilisateurService.setMontantTotale(montant);
    //     List<Paiement> paiementList = paiementRepository.findAll();
    //     for (Paiement paiement : paiementList) {
    //         paiement.setMontantTotal(montant);
    //         paiementRepository.save(paiement);
    //     }
    // }
}
