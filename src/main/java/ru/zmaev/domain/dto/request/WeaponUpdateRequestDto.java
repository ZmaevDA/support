package ru.zmaev.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeaponUpdateRequestDto {
    private Long id;
    private Long attributeId;
    private String name;
    private String description;
    private Double weight;
}
