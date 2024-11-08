package ru.zmaev.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.zmaev.domain.dto.request.InventoryItemCreateRequestDto;
import ru.zmaev.domain.dto.request.InventoryItemUpdateRequestDto;
import ru.zmaev.domain.dto.response.InventoryItemResponseDto;
import ru.zmaev.domain.entity.InventoryItem;

import java.util.List;

public interface InventoryItemService {
    Page<InventoryItemResponseDto> findAll(Pageable pageable);
    List<InventoryItem> findAllById(List<Long> ids);
    InventoryItemResponseDto findById(Long id);
    InventoryItemResponseDto create(InventoryItemCreateRequestDto requestDto);
    InventoryItemResponseDto update(InventoryItemUpdateRequestDto requestDto);
    InventoryItemResponseDto uploadImage(Long id, MultipartFile file);
    void delete(Long id);
}
