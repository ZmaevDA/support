package ru.zmaev.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterResponseDto {
    private Long id;
    private String name;
    private AttributeResponseDto attributes;
}
