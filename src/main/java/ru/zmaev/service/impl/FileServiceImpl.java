package ru.zmaev.service.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.zmaev.commonlib.exception.InternalServerException;
import ru.zmaev.service.FileService;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;

    @Override
    public String uploadFile(MultipartFile file, String bucketName, String fileName) {
        try {
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return fileName;
        } catch (Exception e) {
            throw new InternalServerException("Error while uploading file to Minio: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new InternalServerException("Error while deleting file from Minio" + e.getMessage());
        }
    }
}
