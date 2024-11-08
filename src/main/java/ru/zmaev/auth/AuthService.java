package ru.zmaev.auth;

import ru.zmaev.commonlib.model.dto.response.UserFullResponseDto;
import ru.zmaev.domain.dto.request.UserKeycloakDataDto;

public interface AuthService {

    UserFullResponseDto register(UserKeycloakDataDto dto);
}
