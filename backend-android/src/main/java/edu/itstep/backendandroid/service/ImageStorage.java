package edu.itstep.backendandroid.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {
    void uploadImage(MultipartFile file);//получить на сервер

    byte[] downloadImage(String name);//отправить клиенту

}
