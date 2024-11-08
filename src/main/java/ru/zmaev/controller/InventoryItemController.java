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
import org.springframework.web.multipart.MultipartFile;
import ru.zmaev.controller.api.InventoryItemApi;
import ru.zmaev.domain.dto.request.InventoryItemCreateRequestDto;
import ru.zmaev.domain.dto.request.InventoryItemUpdateRequestDto;
import ru.zmaev.domain.dto.response.InventoryItemResponseDto;
import ru.zmaev.service.InventoryItemService;

@RestController
@RequestMapping("v1/inventory-items")
@Validated
@RequiredArgsConstructor
public class InventoryItemController implements InventoryItemApi {

    private final InventoryItemService inventoryItemService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<InventoryItemResponseDto>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize
    ) {
        Page<InventoryItemResponseDto> requestDtos = inventoryItemService.findAll(PageRequest.of(pagePosition, pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(requestDtos);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<InventoryItemResponseDto> findById(@PathVariable Long id) {
        InventoryItemResponseDto responseDto = inventoryItemService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InventoryItemResponseDto> create(@Valid @RequestBody InventoryItemCreateRequestDto requestDto) {
        InventoryItemResponseDto responseDto = inventoryItemService.create(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InventoryItemResponseDto> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        InventoryItemResponseDto responseDto = inventoryItemService.uploadImage(id, file);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InventoryItemResponseDto> update(@Valid @RequestBody InventoryItemUpdateRequestDto requestDto) {
        InventoryItemResponseDto responseDto = inventoryItemService.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventoryItemService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
