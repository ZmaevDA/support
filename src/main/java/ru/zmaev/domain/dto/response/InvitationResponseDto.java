package ru.zmaev.domain.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class InvitationResponseDto {
    private Long id;
    private Long buildId;
    private String token;
    private List<String> userIds;
}
