package ru.zmaev.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeaponCreateRequestDto {
    private String name;
    private String description;
    private Double weight;
    private Integer vigor;
    private Integer mind;
    private Integer endurance;
    private Integer strength;
    private Integer dexterity;
    private Integer intelligence;
    private Integer faith;
    private Integer arcana;
}
