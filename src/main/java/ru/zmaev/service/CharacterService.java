package ru.zmaev.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zmaev.domain.dto.request.CharacterCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterUpdateRequestDto;
import ru.zmaev.domain.dto.response.CharacterResponseDto;
import ru.zmaev.domain.dto.response.StartClassResponseDto;

import java.util.List;

public interface CharacterService {
    Page<CharacterResponseDto> findAll(Pageable pageable);

    List<StartClassResponseDto> findAllStartClass();

    CharacterResponseDto findById(Long id);

    CharacterResponseDto create(CharacterCreateRequestDto requestDto);

    CharacterResponseDto update(CharacterUpdateRequestDto requestDto);

    void delete(Long id);
}
