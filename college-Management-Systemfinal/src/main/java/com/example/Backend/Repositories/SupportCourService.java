package com.example.Backend.Repositories;

import com.example.Backend.Entities.SupportCours;
import org.springframework.web.multipart.MultipartFile;

public interface SupportCourService {
    SupportCours createSupportCour(SupportCours supportCours, MultipartFile file);
}
