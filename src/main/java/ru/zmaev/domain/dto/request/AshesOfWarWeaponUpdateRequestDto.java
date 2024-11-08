package ru.zmaev.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AshesOfWarWeaponUpdateRequestDto {
    private Long id;
    private Long ashesOfWarId;
    private Long weaponId;
}
