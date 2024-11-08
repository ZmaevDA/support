package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.zmaev.domain.dto.request.AttributeCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterUpdateRequestDto;
import ru.zmaev.domain.dto.request.WeaponCreateRequestDto;
import ru.zmaev.domain.dto.response.AttributeResponseDto;
import ru.zmaev.domain.dto.response.BuildResponseDto;
import ru.zmaev.domain.entity.Attribute;

@Mapper(componentModel = "spring")
public interface AttributeMapper extends EntityMapper<Attribute, AttributeCreateRequestDto, AttributeResponseDto> {

    @Override
    AttributeResponseDto toDto(Attribute entity);

    BuildResponseDto toDto(CharacterUpdateRequestDto dto);

    @Override
    Attribute toEntity(AttributeCreateRequestDto requestDto);

    @Mapping(target = "weapon", ignore = true)
    Attribute toEntity(WeaponCreateRequestDto dto);

    @Mapping(target = "character", ignore = true)
    Attribute toEntity(CharacterCreateRequestDto dto);

    @Mapping(target = "character", ignore = true)
    Attribute toEntity(CharacterUpdateRequestDto dto);

}
