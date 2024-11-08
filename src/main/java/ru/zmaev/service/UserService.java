package ru.zmaev.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zmaev.commonlib.model.dto.common.UserCommonResponseDto;
import ru.zmaev.commonlib.model.dto.response.EntityIsExistsResponseDto;
import ru.zmaev.commonlib.model.dto.response.UserFullResponseDto;
import ru.zmaev.domain.dto.request.UserUpdateRequestDto;
import ru.zmaev.domain.entity.User;

import java.util.List;

public interface UserService {
    UserFullResponseDto getCurrentUserInfo();

    EntityIsExistsResponseDto existsById(String uuid);

    UserFullResponseDto update(String id, UserUpdateRequestDto requestDto);

    UserCommonResponseDto findById(String id, String dtoType);

    UserFullResponseDto findById(String id);

    Page<UserFullResponseDto> findAll(Pageable pageable);

    UserFullResponseDto deleteUser(String id);

    UserFullResponseDto recoverUser(String uuid);

    User findUserByIdOrThrow(String uuid);

    List<User> findUsersByIds(List<String> ids);
}
