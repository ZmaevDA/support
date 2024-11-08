package ru.zmaev.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BuildDetailedResponseDto {
    private Long id;
    private String userId;
    private String buildName;
    private String buildDescription;
    private Double totalWeight;
    private ResistResponseDto totalResist;
    private List<CharacterDetailedResponseDto> characters;
    private List<InventoryItemResponseDto> item;
    private List<WeaponResponseDto> weapon;
}
