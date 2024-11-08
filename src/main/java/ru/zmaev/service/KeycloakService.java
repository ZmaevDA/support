package ru.zmaev.service;

public interface KeycloakService {
    void saveToKeycloak(String keycloakUID, Long userId);
}
