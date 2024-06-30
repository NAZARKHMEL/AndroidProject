package edu.itstep.backendandroid.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class ImageStorageImpl implements ImageStorage {

    @Value("${upload.path}")
    private String uploadPath;
    @Override
    public void uploadImage(MultipartFile file) {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuid = UUID.randomUUID().toString();
        String originalName = file.getOriginalFilename();
        String nameFile = uuid + "." + originalName;

        try (FileOutputStream outputStream = new FileOutputStream(uploadDir + "/" + nameFile)) {
            outputStream.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] downloadImage(String name) {
        File file = new File(uploadPath + "/" + name);
        byte[]fileContent = null;
        try {
            fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileContent;
    }

}
