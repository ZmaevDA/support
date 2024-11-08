package ru.zmaev.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUtils {

    public static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/bmp",
            "image/webp"
    );

    public static boolean isImageFile(MultipartFile file) {
        return ALLOWED_MIME_TYPES.contains(file.getContentType());
    }
}
