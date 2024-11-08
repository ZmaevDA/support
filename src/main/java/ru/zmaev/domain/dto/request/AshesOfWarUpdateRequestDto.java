package ru.zmaev.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AshesOfWarUpdateRequestDto {
    private Long id;
    private String name;
    private String description;
}
