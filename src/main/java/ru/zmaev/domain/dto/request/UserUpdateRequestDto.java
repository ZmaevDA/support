package ru.zmaev.domain.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequestDto {
    @Size(max = 255)
    private String description;
    @Min(18)
    @Max(110)
    private Integer age;
    @Min(0)
    private Integer inGameHours;
    private Long countryId;
}
