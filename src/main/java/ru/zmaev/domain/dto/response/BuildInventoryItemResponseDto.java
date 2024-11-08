package ru.zmaev.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuildInventoryItemResponseDto {
    private Long id;
    private Long inventoryItemId;
}
