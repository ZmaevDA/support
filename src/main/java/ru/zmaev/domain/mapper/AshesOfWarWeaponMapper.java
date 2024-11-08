package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import ru.zmaev.domain.dto.response.AshesOfWarWeaponResponseDto;
import ru.zmaev.domain.entity.AshesOfWarWeapon;

@Mapper(componentModel = "spring")
public interface AshesOfWarWeaponMapper extends EntityMapper<AshesOfWarWeapon, AshesOfWarWeaponResponseDto, AshesOfWarWeaponResponseDto> {
    @Override
    AshesOfWarWeapon toEntity(AshesOfWarWeaponResponseDto requestDto);

    @Override
    AshesOfWarWeaponResponseDto toDto(AshesOfWarWeapon entity);
}
