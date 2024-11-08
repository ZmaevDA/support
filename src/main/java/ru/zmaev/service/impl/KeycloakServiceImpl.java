package ru.zmaev.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.zmaev.service.KeycloakService;

import java.util.List;

@Slf4j
@Service
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak keycloak;
    private final String realm;

    @Autowired
    public KeycloakServiceImpl(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    public void saveToKeycloak(String keycloakUID, Long userId) {
        log.info("Savind userId to keycloak: {}", userId);
        UserResource userResource = getUserResourceByKeycloakUID(keycloakUID);
        UserRepresentation user = userResource.toRepresentation();
        user.getAttributes().put("userId", List.of(String.valueOf(userId)));
        userResource.update(user);
        log.info("userId saved to keycloak: {}", userId);
    }


    private UserResource getUserResourceByKeycloakUID(String keycloakUID) {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users().get(keycloakUID);
    }
}
