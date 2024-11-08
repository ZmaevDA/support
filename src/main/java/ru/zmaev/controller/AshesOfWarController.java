package ru.zmaev.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zmaev.controller.api.AshesOfWarApi;
import ru.zmaev.domain.dto.request.AshesOfWarCreateRequestDto;
import ru.zmaev.domain.dto.request.AshesOfWarUpdateRequestDto;
import ru.zmaev.domain.dto.response.AshesOfWarResponseDto;
import ru.zmaev.service.AshesOfWarService;

@RestController
@RequestMapping("v1/ashes-of-war")
@Validated
@RequiredArgsConstructor
public class AshesOfWarController implements AshesOfWarApi {

    private final AshesOfWarService ashesOfWarService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<AshesOfWarResponseDto>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize
    ) {
        Page<AshesOfWarResponseDto> ashesOfWarPage = ashesOfWarService.findAll(PageRequest.of(pagePosition, pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(ashesOfWarPage);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AshesOfWarResponseDto> findById(@PathVariable Long id) {
        AshesOfWarResponseDto ashesOfWarResponseDto = ashesOfWarService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ashesOfWarResponseDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AshesOfWarResponseDto> create(@Valid @RequestBody AshesOfWarCreateRequestDto requestDto) {
        AshesOfWarResponseDto responseDto = ashesOfWarService.create(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AshesOfWarResponseDto> update(@Valid @RequestBody AshesOfWarUpdateRequestDto requestDto) {
        AshesOfWarResponseDto ashesOfWarResponseDto = ashesOfWarService.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ashesOfWarResponseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ashesOfWarService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
