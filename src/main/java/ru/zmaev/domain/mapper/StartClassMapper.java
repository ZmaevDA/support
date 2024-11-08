package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import ru.zmaev.domain.dto.response.StartClassResponseDto;
import ru.zmaev.domain.entity.StartClass;
@Mapper(componentModel = "spring", uses = {AttributeMapper.class})
public interface StartClassMapper extends EntityMapper<StartClass, StartClassResponseDto, StartClassResponseDto> {
    @Override
    StartClassResponseDto toDto(StartClass entity);
}
