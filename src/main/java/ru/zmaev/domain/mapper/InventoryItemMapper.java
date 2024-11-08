package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.zmaev.domain.dto.request.InventoryItemCreateRequestDto;
import ru.zmaev.domain.dto.request.InventoryItemUpdateRequestDto;
import ru.zmaev.domain.dto.response.InventoryItemResponseDto;
import ru.zmaev.domain.entity.InventoryItem;

@Mapper(componentModel = "spring", uses = {ResistMapper.class})
public interface InventoryItemMapper extends EntityMapper<InventoryItem, InventoryItemCreateRequestDto, InventoryItemResponseDto> {
    @Override
    InventoryItemResponseDto toDto(InventoryItem entity);

    @Override
    @Mapping(target = "resist", ignore = true)
    InventoryItem toEntity(InventoryItemCreateRequestDto requestDto);

    @Mapping(target = "resist", ignore = true)
    InventoryItem toEntity(InventoryItemUpdateRequestDto requestDto);
}
