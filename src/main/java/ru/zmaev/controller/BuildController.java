package ru.zmaev.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zmaev.commonlib.model.dto.response.EntityIsExistsResponseDto;
import ru.zmaev.controller.api.BuildApi;
import ru.zmaev.domain.dto.request.BuildCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildUpdateRequestDto;
import ru.zmaev.domain.dto.response.BuildDetailedResponseDto;
import ru.zmaev.domain.dto.response.BuildResponseDto;
import ru.zmaev.domain.dto.response.CommentResponseDto;
import ru.zmaev.domain.entity.Build_;
import ru.zmaev.domain.entity.Comment_;
import ru.zmaev.domain.enums.ReactionType;
import ru.zmaev.service.BuildService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "v1/builds")
public class BuildController implements BuildApi {

    private final BuildService buildService;

    @Override
    @GetMapping("/public")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<BuildResponseDto>> findAllPublic(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ) {
        Page<BuildResponseDto> builds = buildService.findAllPaged(
                PageRequest.of(pagePosition, pageSize, Sort.by(sortDirection, Build_.RATING)),
                false);
        return ResponseEntity.ok(builds);
    }

    @Override
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<BuildResponseDto>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ) {
        Page<BuildResponseDto> builds = buildService.findAllPaged(
                PageRequest.of(pagePosition, pageSize, Sort.by(sortDirection, Build_.RATING)),
                true);
        return ResponseEntity.ok(builds);
    }

    @Override
    @GetMapping("/{userId}/private")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<BuildResponseDto>> findAllUsersAllowed(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize,
            @PathVariable String userId
    ) {
        Page<BuildResponseDto> builds = buildService.findAllUsersAllowed(PageRequest.of(pagePosition, pageSize), userId);
        return ResponseEntity.ok(builds);
    }

    @Override
    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<BuildResponseDto>> findAllSearch(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam @Size(min = 3) String search
    ) {
        Page<BuildResponseDto> builds = buildService.findAllSearch(search, PageRequest.of(pagePosition, pageSize));
        return ResponseEntity.ok(builds);
    }

    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BuildResponseDto> findById(
            @PathVariable Long id,
            @RequestParam(required = false) String token) {
        BuildResponseDto buildResponseDto = buildService.findById(id, token);
        return ResponseEntity.ok(buildResponseDto);
    }

    @Override
    @GetMapping(value = "/{id}/details")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BuildDetailedResponseDto> findByIdDetailed(
            @PathVariable Long id,
            @RequestParam(required = false) String token) {
        BuildDetailedResponseDto responseDto = buildService.findByIdDetailed(id, token);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<BuildResponseDto> create(@Valid @RequestBody BuildCreateRequestDto requestDto) {
        BuildResponseDto buildResponseDto = buildService.create(requestDto);
        return ResponseEntity.ok(buildResponseDto);
    }

    @Override
    @GetMapping("{id}/comments")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<CommentResponseDto>> findAllCommentsByBuild(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam Sort.Direction sortDirection) {
        Page<CommentResponseDto> responseDto =
                buildService.findAllCommentsByBuildSortedByUpdatedAt(
                        id,
                        PageRequest.of(pagePosition, pageSize, Sort.by(sortDirection, Comment_.UPDATED_AT)));
        return ResponseEntity.ok(responseDto);
    }

    @Override
    @PostMapping("/{id}/users/{userId}/comments")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> addComment(
            @PathVariable Long id,
            @PathVariable String userId,
            @RequestParam String content
    ) {
        buildService.addComment(id, userId, content);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/comments/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id
    ) {
        buildService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{id}/users/{userId}/reactions")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> addReaction(
            @PathVariable Long id,
            @PathVariable String userId,
            @RequestParam ReactionType reactionType
    ) {
        buildService.addReaction(id, userId, reactionType);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("reactions/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> deleteReaction(
            @PathVariable Long id
    ) {
        buildService.deleteReaction(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<BuildResponseDto> update(@Valid @RequestBody BuildUpdateRequestDto requestDto) {
        BuildResponseDto buildResponseDto = buildService.update(requestDto);
        return ResponseEntity.ok(buildResponseDto);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_EDITOR', 'ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        buildService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{id}/exists")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EntityIsExistsResponseDto> isExistById(@PathVariable Long id) {
        EntityIsExistsResponseDto responseDto = buildService.existsById(id);
        return ResponseEntity.ok(responseDto);
    }
}
