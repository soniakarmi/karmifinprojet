package com.example.Backend.Services;

import com.example.Backend.Entities.SupportCours;
import com.example.Backend.Repositories.SupportCourService;
import com.example.Backend.Repositories.SupportCoursRepository;
import com.example.Backend.Utils.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SupportCourIMPL implements SupportCourService {
    private  final SupportCoursRepository supportCoursRepository;
    private  final StorageService storageService;

    public SupportCourIMPL(SupportCoursRepository supportCoursRepository, StorageService storageService) {
        this.supportCoursRepository = supportCoursRepository;
        this.storageService = storageService;
    }




    @Override
    public SupportCours createSupportCour(SupportCours supportCours, MultipartFile file) {
        String img=storageService.storeFile(file);
        supportCours.setImage(img);
        return supportCoursRepository.save(supportCours);
    }
}
