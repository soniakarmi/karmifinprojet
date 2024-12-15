package com.example.Backend.Services;

import com.example.Backend.Entities.*;
import com.example.Backend.Repositories.NotificationRepository;
import com.example.Backend.Repositories.UtilisateurRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final JavaMailSender javaMailSender;
    public void createNotification(String notification, List<Long> to) throws MessagingException {
        for(Long toId:to){
            Optional<Utilisateur> utilisateur=utilisateurRepository.findById(toId);
            if(utilisateur.isPresent()){
                notificationRepository.save(Notification.builder()
                        .notification(notification)
                        .type(TypeNotification.CONTACT)
                        .etudiant((Etudiant) utilisateur.get())
                        .sent_on(sentOn.EMAIL)
                        .build());

                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
                mimeMessageHelper.setTo(utilisateur.get().getEmail());
                mimeMessageHelper.setSubject("Notification");
                String emailBody = String.format("""
                    <div>
                    <h1>%s</h1>
                    </div>
                    """, notification);

                mimeMessageHelper.setText(emailBody, true);

                javaMailSender.send(mimeMessage);
            }
            else {
                throw new EntityNotFoundException("");
            }
        }
    }
}
