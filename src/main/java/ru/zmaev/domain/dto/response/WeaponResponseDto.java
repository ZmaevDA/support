package ru.zmaev.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeaponResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double weight;
    private AttributeResponseDto attributes;
}
