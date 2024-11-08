package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.zmaev.domain.dto.request.WeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.WeaponUpdateRequestDto;
import ru.zmaev.domain.dto.response.WeaponResponseDto;
import ru.zmaev.domain.entity.Weapon;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class})
public interface WeaponMapper extends EntityMapper<Weapon, WeaponCreateRequestDto, WeaponResponseDto> {
    @Override
    @Mapping(target = "attributes", source = "attribute")
    WeaponResponseDto toDto(Weapon entity);

    @Override
    Weapon toEntity(WeaponCreateRequestDto requestDto);

    Weapon toEntity(WeaponUpdateRequestDto requestDto);
}
