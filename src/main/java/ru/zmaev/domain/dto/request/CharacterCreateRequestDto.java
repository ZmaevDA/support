package ru.zmaev.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterCreateRequestDto {
    private Long buildId;
    private Long startClassId;
    private String characterName;
    private AttributeCreateRequestDto attributes;
}
