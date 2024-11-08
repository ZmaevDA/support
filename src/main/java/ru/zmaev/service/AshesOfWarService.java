package ru.zmaev.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zmaev.domain.dto.request.AshesOfWarCreateRequestDto;
import ru.zmaev.domain.dto.request.AshesOfWarUpdateRequestDto;
import ru.zmaev.domain.dto.response.AshesOfWarResponseDto;
import ru.zmaev.domain.entity.AshesOfWar;

public interface AshesOfWarService {

    Page<AshesOfWarResponseDto> findAll(Pageable pageable);

    AshesOfWarResponseDto findById(Long id);

    AshesOfWarResponseDto create(AshesOfWarCreateRequestDto requestDto);

    AshesOfWarResponseDto update(AshesOfWarUpdateRequestDto requestDto);

    void delete(Long id);

    AshesOfWar getAshOfWarOrThrow(Long id);
}
