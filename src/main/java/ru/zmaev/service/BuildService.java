package ru.zmaev.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zmaev.commonlib.model.dto.response.EntityIsExistsResponseDto;
import ru.zmaev.domain.dto.request.BuildCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildUpdateRequestDto;
import ru.zmaev.domain.dto.response.BuildDetailedResponseDto;
import ru.zmaev.domain.dto.response.BuildResponseDto;
import ru.zmaev.domain.dto.response.CommentResponseDto;
import ru.zmaev.domain.entity.Build;
import ru.zmaev.domain.enums.ReactionType;

import java.util.List;

public interface BuildService {

    Page<BuildResponseDto> findAllPaged(Pageable pageable, boolean addPrivate);

    List<Build> findAll();

    Page<BuildResponseDto> findAllSearch(String search, Pageable pageable);

    Page<BuildResponseDto> findAllUsersAllowed(Pageable pageable, String userId);

    BuildResponseDto findById(Long id, String token);

    BuildResponseDto create(BuildCreateRequestDto dto);

    BuildResponseDto update(BuildUpdateRequestDto dto);

    void delete(Long id);

    Build getBuildOrThrow(Long id);

    EntityIsExistsResponseDto existsById(Long id);

    BuildDetailedResponseDto findByIdDetailed(Long id, String token);

    void addReaction(Long buildId, String userId, ReactionType reactionType);

    void addComment(Long buildId, String userId, String content);

    Page<CommentResponseDto> findAllCommentsByBuildSortedByUpdatedAt(Long id, Pageable pageable);

    void deleteComment(Long commentId);

    void recalculateRating(Build build);

    void deleteReaction(Long id);
}
