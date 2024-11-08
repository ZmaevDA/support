package ru.zmaev.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.commonlib.auth.AuthUtils;
import ru.zmaev.commonlib.exception.EntityConflictException;
import ru.zmaev.commonlib.exception.EntityNotFountException;
import ru.zmaev.commonlib.exception.NoAccessException;
import ru.zmaev.commonlib.model.dto.UserInfo;
import ru.zmaev.commonlib.model.dto.common.UserCommonResponseDto;
import ru.zmaev.commonlib.model.dto.response.EntityIsExistsResponseDto;
import ru.zmaev.commonlib.model.dto.response.UserFullResponseDto;
import ru.zmaev.commonlib.model.enums.Role;
import ru.zmaev.domain.dto.request.UserUpdateRequestDto;
import ru.zmaev.domain.entity.Country;
import ru.zmaev.domain.entity.User;
import ru.zmaev.domain.mapper.UserMapper;
import ru.zmaev.repository.CountryRepository;
import ru.zmaev.repository.UserRepository;
import ru.zmaev.service.UserService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    private final UserMapper userMapper;

    private final UserInfo userInfo;

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public UserCommonResponseDto findById(String id, String dtoType) {
        User user = findUserByIdOrThrow(id);
        if (dtoType.equals("inner")) {
            return userMapper.toInnerDto(user);
        }
        return userMapper.toFullDto(user);
    }

    @Override
    public UserFullResponseDto findById(String id) {
        User user = findUserByIdOrThrow(id);
        return userMapper.toFullDto(user);
    }

    @Override
    public Page<UserFullResponseDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toFullDto);
    }

    @Override
    public UserFullResponseDto getCurrentUserInfo() {
        User user = findUserByIdOrThrow(userInfo.getUserId());
        UserFullResponseDto userFullResponseDto = userMapper.toFullDto(user);
        userFullResponseDto.setRoles(userInfo.getRole());
        return userFullResponseDto;
    }

    @Override
    public EntityIsExistsResponseDto existsById(String uuid) {
        EntityIsExistsResponseDto dto = new EntityIsExistsResponseDto();
        dto.setExists(userRepository.existsById(uuid));
        return dto;
    }

    @Override
    @Transactional
    public UserFullResponseDto update(String id, UserUpdateRequestDto requestDto) {
        Country country = countryRepository.findById(requestDto.getCountryId()).orElseThrow(()
                -> new EntityNotFountException("Country", requestDto.getCountryId()));
        User user = findUserByIdOrThrow(id);
        user = userMapper.updateUserFromDto(requestDto, user);
        user.setCountry(country);
        userRepository.save(user);
        UserFullResponseDto responseDto = userMapper.toFullDto(user);
        responseDto.setRoles(userInfo.getRole());
        return responseDto;
    }

    @Override
    @Transactional
    public UserFullResponseDto recoverUser(String uuid) {
        if (!AuthUtils.getIsDeleted()) {
            throw new EntityConflictException("User with username: " + AuthUtils.getUsername() + " is not deleted!");
        }
        User user = findUserByIdOrThrow(uuid);
        user = updateDBUser(false, user);
        updateKeycloakUser(false, uuid);
        return userMapper.toFullDto(user);
    }

    @Override
    @Transactional
    public UserFullResponseDto deleteUser(String uuid) {
        if (AuthUtils.getIsDeleted()) {
            throw new EntityConflictException("User with username: " + AuthUtils.getUsername() + " already deleted!");
        }
        User user = findUserByIdOrThrow(uuid);
        user = updateDBUser(true, user);
        updateKeycloakUser(true, uuid);
        return userMapper.toFullDto(user);
    }

    private User updateDBUser(boolean isDeleted, User user) {
        if (!Objects.equals(user.getUsername(), getCurrentUserInfo().getUsername()) &&
                userInfo.getRole().contains(Role.ROLE_ADMIN.getKeycloakRoleName())
        ) {
            throw new NoAccessException(userInfo.getUsername(), userInfo.getRole().toString());
        }
        user.setIsDeleted(isDeleted);
        user = userRepository.save(user);
        return user;
    }

    private void updateKeycloakUser(boolean isDeleted, String uuid) {
        UserResource userResource = keycloak.realm(realm).users().get(uuid);
        UserRepresentation representation = userResource.toRepresentation();
        Map<String, List<String>> attributes = representation.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }

        List<String> isDeletedAttribute = attributes.get("isDeleted");
        if (isDeletedAttribute == null) {
            isDeletedAttribute = new ArrayList<>();
        }
        isDeletedAttribute.clear();
        isDeletedAttribute.add(String.valueOf(isDeleted));
        attributes.put("isDeleted", isDeletedAttribute);

        representation.setAttributes(attributes);
        userResource.update(representation);
    }

    public User findUserByIdOrThrow(String uuid) {
        return userRepository.findById(uuid).orElseThrow(() ->
                new EntityNotFountException("User", uuid));
    }

    @Override
    public List<User> findUsersByIds(List<String> ids) {
        return userRepository.findAllById(ids);
    }

}
