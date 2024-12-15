package com.example.Backend.Utils;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class PhotoUtility {

    public static String retrievePhoto(Long utilisateurId) throws IOException {
        String uploadDir = "src/main/resources/static/photos";
        String fileNamePrefix = "photo" + utilisateurId + "_";

        Path directoryPath = Paths.get(uploadDir);
        if (!Files.exists(directoryPath)) {
            throw new IOException("Directory not found: " + uploadDir);
        }

        File[] files = directoryPath.toFile().listFiles((dir, name) -> name.startsWith(fileNamePrefix));
        if (files != null && files.length > 0) {
            return files[0].toURI().toURL().toString();
        } else {
            return "Photo not found for utilisateur with ID: " + utilisateurId;
        }
    }

    public static String uploadPhoto(MultipartFile file, Long utilisateurId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String uploadDir = "src/main/resources/static/photos";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = "photo" + utilisateurId + "_" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = uploadPath.resolve(fileName);

        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(file.getBytes());
        }

        return fileName;
    }
}
