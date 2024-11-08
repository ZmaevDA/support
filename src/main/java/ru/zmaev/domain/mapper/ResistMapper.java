package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import ru.zmaev.domain.dto.request.ResistCreateRequestDto;
import ru.zmaev.domain.dto.request.ResistUpdateRequestDto;
import ru.zmaev.domain.dto.response.ResistResponseDto;
import ru.zmaev.domain.entity.Resist;

@Mapper(componentModel = "spring")
public interface ResistMapper extends EntityMapper<Resist, ResistCreateRequestDto, ResistResponseDto> {
    @Override
    Resist toEntity(ResistCreateRequestDto requestDto);

    @Override
    ResistResponseDto toDto(Resist entity);

    Resist toEntity(ResistUpdateRequestDto requestDto);
}
