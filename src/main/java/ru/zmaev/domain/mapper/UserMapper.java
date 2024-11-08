package ru.zmaev.domain.mapper;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.zmaev.commonlib.model.dto.response.UserFullResponseDto;
import ru.zmaev.commonlib.model.dto.response.UserInnerResponseDto;
import ru.zmaev.domain.dto.request.UserRequestDto;
import ru.zmaev.domain.dto.request.UserUpdateRequestDto;
import ru.zmaev.domain.entity.User;

@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface UserMapper extends EntityMapper<User, UserRequestDto, UserFullResponseDto> {

    User updateUserFromDto(@Valid UserUpdateRequestDto dto, @MappingTarget User user);

    UserFullResponseDto toFullDto(User user);

    UserInnerResponseDto toInnerDto(User user);

    @Override
    User toEntity(UserRequestDto requestDto);

    User toEntity(UserFullResponseDto responseDto);
}
