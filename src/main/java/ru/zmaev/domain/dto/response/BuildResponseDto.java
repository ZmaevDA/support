package ru.zmaev.domain.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildResponseDto {
    private Long id;
    private String userId;
    private String buildName;
    private String buildDescription;
    private Boolean isPrivate;
    private List<AshesOfWarWeaponResponseDto> ashesOfWarWeapons;
    private List<BuildInventoryItemResponseDto> buildInventoryItems;
}
