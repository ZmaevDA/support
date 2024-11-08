package ru.zmaev.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zmaev.commonlib.exception.EntityNotFountException;
import ru.zmaev.domain.dto.request.WeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.WeaponUpdateRequestDto;
import ru.zmaev.domain.dto.response.WeaponResponseDto;
import ru.zmaev.domain.entity.Attribute;
import ru.zmaev.domain.entity.Weapon;
import ru.zmaev.domain.mapper.AttributeMapper;
import ru.zmaev.domain.mapper.WeaponMapper;
import ru.zmaev.repository.AttributeRepository;
import ru.zmaev.repository.WeaponRepository;
import ru.zmaev.service.WeaponService;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeaponServiceImpl implements WeaponService {

    private final WeaponRepository weaponRepository;
    private final AttributeRepository attributeRepository;

    private final WeaponMapper weaponMapper;
    private final AttributeMapper attributeMapper;

    @Override
    public WeaponResponseDto create(WeaponCreateRequestDto requestDto) {
        log.info("Creating new weapon and attribute with request: {}", requestDto);
        Weapon weapon = weaponMapper.toEntity(requestDto);
        Attribute attribute = attributeMapper.toEntity(requestDto);
        attributeRepository.save(attribute);
        log.info("Attribute created successfully with id: {}", attribute.getId());
        weapon.setAttribute(attribute);
        weapon = weaponRepository.save(weapon);
        log.info("Weapon created successfully with id: {}", weapon.getId());
        return weaponMapper.toDto(weapon);
    }

    @Override
    public WeaponResponseDto update(WeaponUpdateRequestDto requestDto) {
        log.info("Update weapon with id: {}", requestDto.getId());
        Weapon weapon = getWeaponOrThrow(requestDto.getId());
        Attribute attribute = weapon.getAttribute();
        Weapon newWeapon = weaponMapper.toEntity(requestDto);
        newWeapon.setAttribute(attribute);
        newWeapon = weaponRepository.save(weapon);
        log.info("Weapon updated successfully with id: {}", requestDto.getId());
        return weaponMapper.toDto(newWeapon);
    }

    @Override
    public WeaponResponseDto findById(Long id) {
        Weapon weapon = getWeaponOrThrow(id);
        return weaponMapper.toDto(weapon);
    }

    @Override
    public Page<WeaponResponseDto> findAll(Pageable pageable) {
        Page<Weapon> weapons = weaponRepository.findAll(pageable);
        return weapons.map(weaponMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        Weapon weapon = getWeaponOrThrow(id);
        weaponRepository.delete(weapon);
    }

    @Override
    public Weapon getWeaponOrThrow(Long id) {
        return weaponRepository.findById(id).orElseThrow(
                () -> new EntityNotFountException("Weapon", id));
    }

    private void ifWeaponNotPresentThrow(Long id) {
        if (!weaponRepository.existsById(id)) {
            throw new EntityNotFountException("Weapon", id);
        }
    }
}
