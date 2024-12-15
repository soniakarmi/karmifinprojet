package com.example.Backend.Controllers;

import com.example.Backend.Dto.Requests.NotificationRequest;
import com.example.Backend.Services.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")

@CrossOrigin("*")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/create")
    public ResponseEntity<?> createNotification(@RequestBody NotificationRequest request) throws Exception {
        try{
            notificationService.createNotification(request.getNotification(),request.getTo());
            return ResponseEntity.ok("notification a ete creer avec succes");
        } catch (Exception e) {
            throw new Exception("eereur lors de la creation d'une notification");
        }
    }
}
