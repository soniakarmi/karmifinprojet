package com.example.Backend.Services;

import com.example.Backend.Dto.Requests.QuizFormRequest;
import com.example.Backend.Entities.QuizForm;
import com.example.Backend.Entities.SupportCours;
import com.example.Backend.Entities.TypeQuiz;
import com.example.Backend.Repositories.QuizFormRepository;
import com.example.Backend.Repositories.QuizQuestionRepository;
import com.example.Backend.Repositories.SupportCoursRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuizFormService {

    private final QuizFormRepository quizFormRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final SupportCoursRepository supportCoursRepository;

    public QuizFormService(QuizFormRepository quizFormRepository,
                           QuizQuestionRepository quizQuestionRepository,
                           SupportCoursRepository supportCoursRepository) {
        this.quizFormRepository = quizFormRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.supportCoursRepository = supportCoursRepository;
    }



        @Transactional
        public QuizForm createQuizForm(QuizFormRequest quizFormRequest) {
            // Création et sauvegarde du QuizForm
            QuizForm newQuizForm = new QuizForm();
            newQuizForm.setType(quizFormRequest.getType());



            // Vérification du supportcours
            SupportCours supportCours = supportCoursRepository.findById(quizFormRequest.getSupportcoursId())
                    .orElseThrow(() -> new EntityNotFoundException("Supportcour avec ID " + quizFormRequest.getSupportcoursId()+ " introuvable"));

            // Définition du coefficient selon le type de quiz
            TypeQuiz typeQuiz = quizFormRequest.getType();
            if (typeQuiz == TypeQuiz.DS) {
                newQuizForm.setCoeif(0.3F);
            } else if (typeQuiz == TypeQuiz.EXAMEN) {
                newQuizForm.setCoeif(0.5F);
            } else {
                throw new IllegalArgumentException("Type de quiz non supporté : " + typeQuiz);
            }

            // Associer le support de cours
           // List<SupportCours> supportCoursList = cours.getSupportCoursList();
           // if (supportCoursList != null && !supportCoursList.isEmpty()) {

             //   newQuizForm.setSupportCours(supportCoursList.get(0));
            //} else {
              //  throw new EntityNotFoundException("Aucun support de cours trouvé pour le cours avec ID " + quizFormRequest.getCoursId());
            //}
            // Associer le support de cours directement
            newQuizForm.setSupportCours(supportCours);

            // Mettre à jour la date de dernière modification
            newQuizForm.setLastUpdated_at(LocalDate.now());

            // Sauvegarder le QuizForm
            return quizFormRepository.save(newQuizForm);

    }


  /*  @Transactional
    public QuizForm createQuizForm(QuizFormRequest quizFormRequest) {
        // Vérification des données de la requête
        if (quizFormRequest == null) {
            throw new IllegalArgumentException("La requête QuizFormRequest ne peut pas être null.");
        }

        // Vérification du type de quiz
        TypeQuiz typeQuiz = quizFormRequest.getType();
        if (typeQuiz == null) {
            throw new IllegalArgumentException("Le type de quiz ne peut pas être null.");
        }

        // Vérification de l'existence du support de cours
        SupportCours supportCours = supportCoursRepository.findById(quizFormRequest.getSupportcoursId())
                .orElseThrow(() -> new EntityNotFoundException("Supportcours avec ID " + quizFormRequest.getSupportcoursId() + " introuvable"));

        // Création et sauvegarde du QuizForm
        QuizForm newQuizForm = new QuizForm();
        newQuizForm.setType(typeQuiz);

        // Définition du coefficient selon le type de quiz
        switch (typeQuiz) {
            case DS:
                newQuizForm.setCoeif(0.3F);
                break;
            case EXAMEN:
                newQuizForm.setCoeif(0.5F);
                break;
            default:
                throw new IllegalArgumentException("Type de quiz non supporté : " + typeQuiz);
        }

        // Associer le support de cours
        newQuizForm.setSupportCours(supportCours);

        // Mettre à jour la date de dernière modification
        newQuizForm.setLastUpdated_at(LocalDate.now());

        // Sauvegarder le QuizForm
        return quizFormRepository.save(newQuizForm);
    }

*/


    // Suppression d'un QuizForm par son ID
    public void deleteQuizForm(Long quizFormId) {
        if (!quizFormRepository.existsById(quizFormId)) {
            throw new EntityNotFoundException("Quiz form avec ID " + quizFormId + " introuvable");
        }
        quizFormRepository.deleteById(quizFormId);
    }


    // Trouver tous les QuizForms par coursId et typeQuiz
  //  public List<QuizForm> findAllByCoursIdAndTypeQuiz(Long coursId, TypeQuiz typeQuiz) {
    //    return quizFormRepository.findAllByCoursIdAndTypeQuiz(coursId, typeQuiz);
    //}

    public List<QuizForm> findAllBysupportcourIdAndTypeQuiz(Long suppportcourId, TypeQuiz typeQuiz) {
            return quizFormRepository.findAllBySupportcourIdAndTypeQuiz(suppportcourId, typeQuiz);
        }
}
