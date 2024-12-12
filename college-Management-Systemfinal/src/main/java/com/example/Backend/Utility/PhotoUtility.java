package com.example.Backend.Utility;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class PhotoUtility {
    public static String retrievePhoto(Long utilisateurId) throws IOException {
        String uploadDir = "src/main/resources/static/photos";
        String fileName = "photo" + utilisateurId + "_";

        try {
            Path directoryPath = Paths.get(uploadDir);
            if (!Files.exists(directoryPath)) {
                throw new FileNotFoundException("Directory not found: " + uploadDir);
            }

            File[] files = directoryPath.toFile().listFiles((dir, name) -> name.startsWith(fileName));
            if (files != null && files.length > 0) {
                File photoFile = files[0];
                return photoFile.toURI().toURL().toString();
            } else {
                return "Photo not found for utilisateur with ID: " + utilisateurId;
            }
        } catch (IOException ex) {
            return "Failed to retrieve photo: " + ex.getMessage();
        }
    }
    public static String uploadPhoto(MultipartFile file, Long utilisateurId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String uploadDir = "src/main/resources/static/photos";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            String fileName = "photo" + utilisateurId + "_" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String filePath = uploadDir + File.separator + fileName;

            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(file.getBytes());
            fos.close();

            return fileName;
        } catch (IOException ex) {
            throw new IOException("Failed to upload photo: " + ex.getMessage());
        }


    }
}
