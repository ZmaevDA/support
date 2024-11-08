package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.zmaev.domain.dto.request.CharacterCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterUpdateRequestDto;
import ru.zmaev.domain.dto.response.CharacterDetailedResponseDto;
import ru.zmaev.domain.dto.response.CharacterResponseDto;
import ru.zmaev.domain.entity.Character;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, StartClassMapper.class})
public interface CharacterMapper extends EntityMapper<Character, CharacterCreateRequestDto, CharacterResponseDto> {
    @Override

    @Mapping(target = "attributes", source = "attribute")
    CharacterResponseDto toDto(Character entity);

    CharacterDetailedResponseDto toDetailedDto(Character character);

    @Override
    @Mapping(target = "name", source = "characterName")
    Character toEntity(CharacterCreateRequestDto requestDto);

    @Mapping(target = "name", source = "characterName")
    @Mapping(target = "id", source = "characterId")
    Character toEntity(CharacterUpdateRequestDto requestDto);
}
