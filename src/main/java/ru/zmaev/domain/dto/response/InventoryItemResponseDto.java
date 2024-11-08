package ru.zmaev.domain.dto.response;

import lombok.Data;
import ru.zmaev.domain.enums.ItemType;

@Data
public class InventoryItemResponseDto {
    private Long id;
    private ResistResponseDto resist;
    private String name;
    private String description;
    private Double weight;
    private ItemType itemType;
    private String imageUrl;
}
