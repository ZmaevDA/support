package ru.zmaev.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartClassResponseDto {
    private Long id;
    private AttributeResponseDto attribute;
    private String name;
    private Integer level;
    private Integer health;
    private Integer mana;
    private Integer stamina;

}
