package ru.zmaev.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Base64Utils {

    private Base64Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static String encoder(String password) {
        String hashedPassword = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            hashedPassword = Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
        return hashedPassword;
    }
}
