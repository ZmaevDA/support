package ru.zmaev.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.zmaev.commonlib.exception.EntityBadRequestException;
import ru.zmaev.commonlib.exception.EntityNotFountException;
import ru.zmaev.domain.dto.request.InventoryItemCreateRequestDto;
import ru.zmaev.domain.dto.request.InventoryItemUpdateRequestDto;
import ru.zmaev.domain.dto.response.InventoryItemResponseDto;
import ru.zmaev.domain.entity.InventoryItem;
import ru.zmaev.domain.entity.Resist;
import ru.zmaev.domain.mapper.InventoryItemMapper;
import ru.zmaev.domain.mapper.ResistMapper;
import ru.zmaev.repository.InventoryItemRepository;
import ru.zmaev.repository.ResistRepository;
import ru.zmaev.service.FileService;
import ru.zmaev.service.InventoryItemService;
import ru.zmaev.util.FileUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

    @Value("${minio.inventoryItemBucketName}")
    private String inventoryItemBucketName;

    private final InventoryItemRepository inventoryItemRepository;
    private final ResistRepository resistRepository;

    private final InventoryItemMapper inventoryItemMapper;
    private final ResistMapper resistMapper;

    private final FileService fileService;

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryItemResponseDto> findAll(Pageable pageable) {
        Page<InventoryItem> inventoryItems = inventoryItemRepository.findAll(pageable);
        return inventoryItems.map(inventoryItemMapper::toDto);
    }

    @Override
    public List<InventoryItem> findAllById(List<Long> ids) {
        return inventoryItemRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItemResponseDto findById(Long id) {
        InventoryItem inventoryItem = findInventoryItemOrThrow(id);
        return inventoryItemMapper.toDto(inventoryItem);
    }

    @Override
    @Transactional
    public InventoryItemResponseDto create(InventoryItemCreateRequestDto requestDto) {
        InventoryItem item = inventoryItemMapper.toEntity(requestDto);
        Resist resist = resistMapper.toEntity(requestDto.getResist());
        resist = resistRepository.save(resist);
        item.setResist(resist);
        item = inventoryItemRepository.save(item);
        return inventoryItemMapper.toDto(item);
    }

    @Override
    @Transactional
    public InventoryItemResponseDto update(InventoryItemUpdateRequestDto requestDto) {
        ifInventoryItemNotPresentThrow(requestDto.getId());
        ifResistNotPresentThrow(requestDto.getResist().getId());
        InventoryItem item = inventoryItemMapper.toEntity(requestDto);
        Resist resist = resistMapper.toEntity(requestDto.getResist());
        resist = resistRepository.save(resist);
        item.setResist(resist);
        item = inventoryItemRepository.save(item);
        return inventoryItemMapper.toDto(item);
    }

    @Override
    @Transactional
    public InventoryItemResponseDto uploadImage(Long id, MultipartFile file) {
        if (!FileUtils.isImageFile(file)) {
            throw new EntityBadRequestException("Invalid file type. Only image files are allowed.");
        }
        InventoryItem inventoryItem = findInventoryItemOrThrow(id);
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String oldImageUrl = inventoryItem.getImageUrl();
        String fileName = id + "/image" + fileExtension;
        inventoryItem.setImageUrl(inventoryItemBucketName + "/" + fileName);
        inventoryItemRepository.save(inventoryItem);
        if (oldImageUrl != null) {
            fileService.deleteFile(
                    inventoryItemBucketName,
                    oldImageUrl.replaceAll(inventoryItemBucketName + "/", ""));
        }
        fileService.uploadFile(file, inventoryItemBucketName, fileName);
        return inventoryItemMapper.toDto(inventoryItem);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ifInventoryItemNotPresentThrow(id);
        inventoryItemRepository.findById(id).ifPresent(inventoryItemRepository::delete);
    }

    private InventoryItem findInventoryItemOrThrow(Long id) {
        return inventoryItemRepository.findById(id).orElseThrow(() ->
                new EntityNotFountException("InventoryItem", id));
    }

    private void ifInventoryItemNotPresentThrow(Long id) {
        if (!inventoryItemRepository.existsById(id)) {
            throw new EntityNotFountException("InventoryItem", id);
        }
    }

    private void ifResistNotPresentThrow(Long id) {
        if (!resistRepository.existsById(id)) {
            throw new EntityNotFountException("Resist", id);
        }
    }
}
