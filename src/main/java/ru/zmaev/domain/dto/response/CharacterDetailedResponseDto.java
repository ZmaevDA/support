package ru.zmaev.domain.dto.response;

import lombok.Data;

@Data
public class CharacterDetailedResponseDto {
    private Long id;
    private String name;
    private Integer level;
    private Long hp;
    private Long fp;
    private Long stamina;
    private AttributeResponseDto attribute;
    private StartClassResponseDto startClass;
}

