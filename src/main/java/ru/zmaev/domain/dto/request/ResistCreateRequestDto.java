package ru.zmaev.domain.dto.request;

import lombok.Data;

@Data
public class ResistCreateRequestDto {
    private Integer vitality;
    private Integer focus;
    private Integer robustness;
    private Integer immunity;
    private Integer holy;
    private Integer lightning;
    private Integer fire;
    private Integer magic;
    private Integer pierce;
    private Integer splash;
    private Integer strike;
    private Integer physical;
}
