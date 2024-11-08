package ru.zmaev.domain.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentResponseDto {
    private Long id;
    private String userId;
    private Long buildId;
    private Instant commentedAt;
    private Instant updatedAt;
    private String content;
}
