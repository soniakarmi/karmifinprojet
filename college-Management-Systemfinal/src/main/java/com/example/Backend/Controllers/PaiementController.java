package com.example.Backend.Controllers;

import com.example.Backend.Dto.Responses.PaiementResponse;
import com.example.Backend.Services.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PaiementController {
    private final PaiementService paiementService;

    @PostMapping("/generatepaye")
    public ResponseEntity<String> generatePaymentLink(@RequestParam Long etudiant_id, @RequestParam Double montant) {
        try {
            // Generate the payment link using the service
            String paymentLink = paiementService.generatePaymentLink(etudiant_id, montant);

            // If the payment link is generated successfully, return it
            if (paymentLink != null && !paymentLink.isEmpty()) {
                return ResponseEntity.ok("Payment link generated: " + paymentLink);
            } else {
                throw new Exception("Payment link generation failed.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 2. API for processing the payment
    @PostMapping("/payerprocess")
    public ResponseEntity<String> payerProcess(@RequestParam Long etudiantId,
                                               @RequestParam Double montant,
                                               @RequestParam String paymentId) {
        try {
            // Call the external API to check the payment status
            Map<String, Object> paymentStatus = paiementService.checkPaymentStatus(paymentId);

            // Extract the status from the paymentStatus map
            String status = (String) paymentStatus.get("status");

            // If the status is 'completed', proceed with the payment
            if ("completed".equals(status)) {
                String result = paiementService.payer(etudiantId, montant);
                return ResponseEntity.ok(result);
            } else if ("pending".equals(status)) {
                // Do nothing if the status is pending
                return ResponseEntity.status(HttpStatus.OK).body("Payment is still pending. No action taken.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment status: " + status);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    // Method to call external API and get payment status
    // private String checkPaymentStatus(String paymentId) {
    //     String url = "https://api.preprod.konnect.network/api/v2/payments/" + paymentId;

    //     RestTemplate restTemplate = new RestTemplate();
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Authorization", "Bearer YOUR_API_KEY");  // Set your API key here

    //     HttpEntity<String> entity = new HttpEntity<>(headers);

    //     try {
    //         ResponseEntity<KonnectPaymentResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, KonnectPaymentResponse.class);
    //         if (response.getStatusCode() == HttpStatus.OK) {
    //             KonnectPaymentResponse paymentResponse = response.getBody();
    //             if (paymentResponse != null) {
    //                 return paymentResponse.getStatus();
    //             } else {
    //                 throw new Exception("Empty response from payment API.");
    //             }
    //         } else {
    //             throw new Exception("Failed to retrieve payment status from API. HTTP Status: " + response.getStatusCode());
    //         }
    //     } catch (Exception e) {
    //         throw new RuntimeException("Error while calling payment status API", e);
    //     }
    // }


    // @PostMapping("/set-nombre-tranche")
    // public ResponseEntity<Void> setNombreTranche(@RequestParam int nombre) {
    //     paiementService.setNombreTranche(nombre);
    //     return ResponseEntity.ok().build();
    // }

    @GetMapping("/details")
    public ResponseEntity<PaiementResponse> getPaiementDetails() throws Exception {
        PaiementResponse paiementResponse = paiementService.paiement();
        return ResponseEntity.ok(paiementResponse);
    }

    // @PostMapping("/set-montant-totale")
    // public ResponseEntity<String> setMontantTotale(@RequestParam Double montant) {
    //     try {
    //         paiementService.setMontantTotale(montant);
    //         return ResponseEntity.ok("Montant total mis à jour avec succès");
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).body("Erreur lors de la mise à jour du montant total: " + e.getMessage());
    //     }
    // }
}
