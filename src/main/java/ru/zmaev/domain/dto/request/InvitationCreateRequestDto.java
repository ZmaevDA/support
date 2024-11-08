package ru.zmaev.domain.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class InvitationCreateRequestDto {
    private Long buildId;
    private List<String> userIds;
}
