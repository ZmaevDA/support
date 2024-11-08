package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import ru.zmaev.commonlib.model.dto.response.CountryResponseDto;
import ru.zmaev.domain.entity.Country;

@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<Country, CountryResponseDto, CountryResponseDto> {
    @Override
    CountryResponseDto toDto(Country entity);

    @Override
    Country toEntity(CountryResponseDto requestDto);
}
