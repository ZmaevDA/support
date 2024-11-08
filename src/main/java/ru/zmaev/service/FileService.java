package ru.zmaev.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file, String bucketName, String fileName);
    void deleteFile(String bucketName, String fileName);
}
