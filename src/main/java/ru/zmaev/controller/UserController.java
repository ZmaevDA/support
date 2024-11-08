package ru.zmaev.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zmaev.commonlib.model.dto.common.UserCommonResponseDto;
import ru.zmaev.commonlib.model.dto.response.EntityIsExistsResponseDto;
import ru.zmaev.commonlib.model.dto.response.UserFullResponseDto;
import ru.zmaev.controller.api.UserApi;
import ru.zmaev.domain.dto.request.UserUpdateRequestDto;
import ru.zmaev.service.UserService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("v1/users/")
public class UserController implements UserApi {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserFullResponseDto>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize
    ) {
        return ResponseEntity.ok(userService.findAll(PageRequest.of(pagePosition, pageSize)));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserCommonResponseDto> findById(
            @PathVariable String uuid,
            @RequestHeader(value = "dtoType", required = false, defaultValue = "full") String dtoType) {
        return ResponseEntity.ok(userService.findById(uuid, dtoType));
    }

    @GetMapping("/{uuid}/exists")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EntityIsExistsResponseDto> existsById(@PathVariable String uuid) {
        return ResponseEntity.ok(userService.existsById(uuid));
    }

    @GetMapping("/me")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserFullResponseDto> getUserInfo() {
        return ResponseEntity.ok(userService.getCurrentUserInfo());
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserFullResponseDto> update(@PathVariable String uuid,
                                                      @Valid @RequestBody UserUpdateRequestDto requestDto) {
        return ResponseEntity.ok(userService.update(uuid, requestDto));
    }

    @DeleteMapping("/{uuid}/deletion")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserFullResponseDto> delete(@PathVariable String uuid) {
        return ResponseEntity.ok(userService.deleteUser(uuid));
    }

    @PostMapping("/{uuid}/recovery")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserFullResponseDto> recover(@PathVariable String uuid) {
        return ResponseEntity.ok(userService.recoverUser(uuid));
    }
}
