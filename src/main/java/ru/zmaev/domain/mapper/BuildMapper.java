package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.zmaev.domain.document.BuildDocument;
import ru.zmaev.domain.dto.request.BuildCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildUpdateRequestDto;
import ru.zmaev.domain.dto.response.AshesOfWarWeaponResponseDto;
import ru.zmaev.domain.dto.response.BuildInventoryItemResponseDto;
import ru.zmaev.domain.dto.response.BuildResponseDto;
import ru.zmaev.domain.entity.AshesOfWarWeapon;
import ru.zmaev.domain.entity.Build;
import ru.zmaev.domain.entity.BuildInventoryItem;
import ru.zmaev.domain.entity.BuildWeapon;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {AshesOfWarWeaponMapper.class})
public interface BuildMapper extends EntityMapper<Build, BuildCreateRequestDto, BuildResponseDto> {

    BuildDocument toDocument(Build build);

    BuildResponseDto toDto(BuildDocument buildDocument);

    @Mapping(target = "name", source = "buildName")
    @Mapping(target = "description", source = "buildDescription")
    @Mapping(target = "user", ignore = true)
    Build toEntity(BuildCreateRequestDto requestDto);

    @Mapping(target = "name", source = "buildName")
    @Mapping(target = "description", source = "buildDescription")
    Build toEntity(BuildUpdateRequestDto requestDto);

    @Mapping(source = "name", target = "buildName")
    @Mapping(source = "description", target = "buildDescription")
    @Mapping(source = "user.id", target = "userId")
    BuildResponseDto toDto(Build entity);

    @Named("buildWeaponsToAshesOfWarWeaponResponseDtos")
    default List<AshesOfWarWeaponResponseDto> buildWeaponsToAshesOfWarWeaponResponseDtos(Set<BuildWeapon> buildWeapons) {
        if (buildWeapons == null) {
            return Collections.emptyList();
        }
        return buildWeapons.stream()
                .map(buildWeapon -> ashesOfWarWeaponToAshesOfWarWeaponResponseDto(buildWeapon.getAshesOfWarWeapon()))
                .toList();
    }

    @Named("buildToBuildResponseDto")
    default BuildResponseDto buildToBuildResponseDto(Build build) {
        if (build == null) {
            return null;
        }
        List<AshesOfWarWeaponResponseDto> ashesOfWarWeapons = buildWeaponsToAshesOfWarWeaponResponseDtos(build.getBuildWeapons());
        List<BuildInventoryItemResponseDto> buildInventoryItems = build.getBuildInventoryItems().stream()
                .map(this::buildInventoryItemToBuildInventoryItemResponseDto)
                .toList();

        return new BuildResponseDto(
                build.getId(),
                build.getUser().getId(),
                build.getName(),
                build.getDescription(),
                build.getIsPrivate(),
                ashesOfWarWeapons,
                buildInventoryItems
        );
    }

    default BuildInventoryItemResponseDto buildInventoryItemToBuildInventoryItemResponseDto(BuildInventoryItem buildInventoryItem) {
        if (buildInventoryItem == null) {
            return null;
        }
        return new BuildInventoryItemResponseDto(
                buildInventoryItem.getId(),
                buildInventoryItem.getInventoryItem().getId()
        );
    }

    default AshesOfWarWeaponResponseDto ashesOfWarWeaponToAshesOfWarWeaponResponseDto(AshesOfWarWeapon ashesOfWarWeapon) {
        if (ashesOfWarWeapon == null) {
            return null;
        }
        return new AshesOfWarWeaponResponseDto(
                ashesOfWarWeapon.getId(),
                ashesOfWarWeapon.getAshesOfWar().getId(),
                ashesOfWarWeapon.getWeapon().getId()
        );
    }
}
