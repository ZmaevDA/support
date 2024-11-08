package ru.zmaev.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttributeCreateRequestDto {
    private Integer vigor;
    private Integer mind;
    private Integer endurance;
    private Integer strength;
    private Integer dexterity;
    private Integer intelligence;
    private Integer faith;
    private Integer arcana;
}
