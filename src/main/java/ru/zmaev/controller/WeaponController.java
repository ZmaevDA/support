package ru.zmaev.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zmaev.controller.api.WeaponApi;
import ru.zmaev.domain.dto.request.WeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.WeaponUpdateRequestDto;
import ru.zmaev.domain.dto.response.WeaponResponseDto;
import ru.zmaev.service.WeaponService;

@RestController
@RequestMapping("v1/weapons")
@Validated
@RequiredArgsConstructor
public class WeaponController implements WeaponApi {

    private final WeaponService weaponService;

    @GetMapping
    public ResponseEntity<Page<WeaponResponseDto>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(weaponService.findAll(PageRequest.of(pagePosition, pageSize)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeaponResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(weaponService.findById(id));
    }

    @PostMapping
    public ResponseEntity<WeaponResponseDto> create(@RequestBody WeaponCreateRequestDto weaponCreateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(weaponService.create(weaponCreateRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WeaponResponseDto> update(@RequestBody WeaponUpdateRequestDto weaponRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(weaponService.update(weaponRequestDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        weaponService.delete(id);
        ResponseEntity.status(HttpStatus.OK).build();
    }
}
