package com.example.Backend.Repositories;

import com.example.Backend.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
public interface SupportCoursRepository extends JpaRepository<SupportCours, Long> {
    List<SupportCours> findAllByCours(Cours cours);

    List<SupportCours> findAllByCoursAndTypeSupportCours(Cours cours, TypeQuiz type);
 //   List<SupportCours> findAllByCoursAndType(Cours cours, TypeQuiz type);
 //SupportCours  createSupportCous(SupportCours supportCours, MultipartFile file);
}
