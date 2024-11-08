package ru.zmaev.domain.dto.request;

import lombok.Data;
import ru.zmaev.domain.enums.ItemType;

@Data
public class InventoryItemCreateRequestDto {
    private String name;
    private String description;
    private Double weight;
    private ItemType itemType;
    private ResistCreateRequestDto resist;
}
