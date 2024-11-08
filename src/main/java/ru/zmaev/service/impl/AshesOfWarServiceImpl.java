package ru.zmaev.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.commonlib.exception.EntityNotFountException;
import ru.zmaev.domain.dto.request.AshesOfWarCreateRequestDto;
import ru.zmaev.domain.dto.request.AshesOfWarUpdateRequestDto;
import ru.zmaev.domain.dto.response.AshesOfWarResponseDto;
import ru.zmaev.domain.entity.AshesOfWar;
import ru.zmaev.domain.mapper.AshesOfWarMapper;
import ru.zmaev.repository.AshesOfWarRepository;
import ru.zmaev.service.AshesOfWarService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AshesOfWarServiceImpl implements AshesOfWarService {

    private final AshesOfWarRepository ashesOfWarRepository;

    private final AshesOfWarMapper ashesOfWarMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<AshesOfWarResponseDto> findAll(Pageable pageable) {
        Page<AshesOfWar> ashesOfWarPage = ashesOfWarRepository.findAll(pageable);
        return ashesOfWarPage.map(ashesOfWarMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AshesOfWarResponseDto findById(Long id) {
        AshesOfWar ashesOfWar = ashesOfWarRepository.findById(id).orElseThrow(() ->
                new EntityNotFountException("AshesOfWar", id));
        return ashesOfWarMapper.toDto(ashesOfWar);
    }

    @Override
    @Transactional
    public AshesOfWarResponseDto create(AshesOfWarCreateRequestDto requestDto) {
        log.info("Creating new AshOfWar with request: {}", requestDto);
        AshesOfWar ashesOfWar = ashesOfWarMapper.toEntity(requestDto);
        ashesOfWar = ashesOfWarRepository.save(ashesOfWar);
        log.info("AshOfWar created successfully with id: {}", ashesOfWar.getId());
        return ashesOfWarMapper.toDto(ashesOfWar);
    }

    @Override
    @Transactional
    public AshesOfWarResponseDto update(AshesOfWarUpdateRequestDto requestDto) {
        log.info("Update AshOfWar with id: {}", requestDto.getId());
        ifAshOfWarNotPresentThrow(requestDto.getId());
        AshesOfWar ashesOfWar = ashesOfWarMapper.toEntity(requestDto);
        ashesOfWar = ashesOfWarRepository.save(ashesOfWar);
        log.info("AshOfWar updated successfully with id: {}", requestDto.getId());
        return ashesOfWarMapper.toDto(ashesOfWar);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting AshOfWar successfully with id: {}", id);
        ashesOfWarRepository.findById(id).ifPresent(ashesOfWarRepository::delete);
        log.info("AshOfWar deleted successfully with id: {}", id);
    }

    @Override
    public AshesOfWar getAshOfWarOrThrow(Long id) {
        return ashesOfWarRepository.findById(id).orElseThrow(() ->
                new EntityNotFountException("AshesOfWar", id));
    }

    private void ifAshOfWarNotPresentThrow(Long id) {
        if (!ashesOfWarRepository.existsById(id)) {
            throw new EntityNotFountException("AshesOfWar", id);
        }
    }
}
