package ru.zmaev.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AshesOfWarWeaponCreateRequestDto {
    private Long ashesOfWarId;
    private Long weaponId;
}