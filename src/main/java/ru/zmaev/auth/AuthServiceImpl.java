package ru.zmaev.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.commonlib.exception.EntityConflictException;
import ru.zmaev.commonlib.model.dto.response.UserFullResponseDto;
import ru.zmaev.domain.dto.request.UserKeycloakDataDto;
import ru.zmaev.domain.entity.User;
import ru.zmaev.domain.mapper.UserMapper;
import ru.zmaev.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional
    public UserFullResponseDto register(UserKeycloakDataDto dto) {
        log.info("Creating new user with keycloak id: {}", dto.getKeycloakUID());

        if (userRepository.existsById(dto.getUsername())) {
            throw new EntityConflictException("User with id '" + dto.getKeycloakUID() + "' already exists!");
        }

        User user = new User();
        user.setId(dto.getKeycloakUID());
        user.setUsername(dto.getUsername());
        user.setIsDeleted(false);
        user.setEmail(dto.getEmail());

        user = userRepository.save(user);
        log.info("New user saved to database: {}", user);
        return userMapper.toFullDto(user);
    }
}
