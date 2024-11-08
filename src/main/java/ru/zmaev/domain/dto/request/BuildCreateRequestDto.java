package ru.zmaev.domain.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildCreateRequestDto {
    private String buildName;
    private String buildDescription;
    private List<Long> inventoryItemIds;
    private Boolean isPrivate;
    @Size(min = 1, max = 4)
    private List<AshesOfWarWeaponCreateRequestDto> ashesOfWarWeapons;
}
