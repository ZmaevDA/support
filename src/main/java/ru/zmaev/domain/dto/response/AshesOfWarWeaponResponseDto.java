package ru.zmaev.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AshesOfWarWeaponResponseDto {
    private Long id;
    private Long ashesOfWarId;
    private Long weaponId;
}
