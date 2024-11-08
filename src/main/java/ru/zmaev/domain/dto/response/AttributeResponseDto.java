package ru.zmaev.domain.dto.response;

import lombok.Data;

@Data
public class AttributeResponseDto {
    private Long id;
    private Integer vigor;
    private Integer mind;
    private Integer endurance;
    private Integer strength;
    private Integer dexterity;
    private Integer intelligence;
    private Integer faith;
    private Integer arcana;
}
