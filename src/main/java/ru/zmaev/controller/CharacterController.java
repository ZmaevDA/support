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
import ru.zmaev.controller.api.CharacterApi;
import ru.zmaev.domain.dto.request.CharacterCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterUpdateRequestDto;
import ru.zmaev.domain.dto.response.CharacterResponseDto;
import ru.zmaev.domain.dto.response.StartClassResponseDto;
import ru.zmaev.service.CharacterService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("v1/characters")
public class CharacterController implements CharacterApi {

    private final CharacterService characterService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<CharacterResponseDto>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize
    ) {
        Page<CharacterResponseDto> characters = characterService.findAll(PageRequest.of(pagePosition, pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(characters);
    }

    @GetMapping("/start-classes")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<StartClassResponseDto>> findAllStartClasses() {
        List<StartClassResponseDto> startClass = characterService.findAllStartClass();
        return ResponseEntity.status(HttpStatus.OK).body(startClass);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CharacterResponseDto> findById(@PathVariable Long id) {
        CharacterResponseDto characterResponseDto = characterService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(characterResponseDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<CharacterResponseDto> create(@Valid @RequestBody CharacterCreateRequestDto requestDto) {
        CharacterResponseDto characterResponseDto = characterService.create(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(characterResponseDto);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<CharacterResponseDto> update(@Valid @RequestBody CharacterUpdateRequestDto requestDto) {
        CharacterResponseDto characterResponseDto = characterService.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(characterResponseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_EDITOR', 'ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        characterService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
