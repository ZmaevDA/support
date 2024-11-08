package ru.zmaev.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zmaev.domain.dto.request.WeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.WeaponUpdateRequestDto;
import ru.zmaev.domain.dto.response.WeaponResponseDto;
import ru.zmaev.domain.entity.Weapon;

public interface WeaponService {
    Page<WeaponResponseDto> findAll(Pageable pageable);
    WeaponResponseDto findById(Long id);
    WeaponResponseDto create(WeaponCreateRequestDto requestDto);
    WeaponResponseDto update(WeaponUpdateRequestDto requestDto);
    void delete(Long id);

    Weapon getWeaponOrThrow(Long id);
}
