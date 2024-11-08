package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import ru.zmaev.domain.dto.request.AshesOfWarCreateRequestDto;
import ru.zmaev.domain.dto.request.AshesOfWarUpdateRequestDto;
import ru.zmaev.domain.dto.response.AshesOfWarResponseDto;
import ru.zmaev.domain.entity.AshesOfWar;

@Mapper(componentModel = "spring")
public interface AshesOfWarMapper extends EntityMapper<AshesOfWar, AshesOfWarCreateRequestDto, AshesOfWarResponseDto> {
    @Override
    AshesOfWar toEntity(AshesOfWarCreateRequestDto requestDto);

    AshesOfWar toEntity(AshesOfWarUpdateRequestDto requestDto);

    @Override
    AshesOfWarResponseDto toDto(AshesOfWar entity);
}
