package ru.zmaev.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.commonlib.exception.EntityBadRequestException;
import ru.zmaev.commonlib.exception.EntityNotFountException;
import ru.zmaev.commonlib.exception.NoAccessException;
import ru.zmaev.commonlib.model.dto.UserInfo;
import ru.zmaev.commonlib.model.enums.Role;
import ru.zmaev.domain.dto.request.AttributeCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterUpdateRequestDto;
import ru.zmaev.domain.dto.response.CharacterResponseDto;
import ru.zmaev.domain.dto.response.StartClassResponseDto;
import ru.zmaev.domain.entity.Attribute;
import ru.zmaev.domain.entity.Build;
import ru.zmaev.domain.entity.Character;
import ru.zmaev.domain.entity.StartClass;
import ru.zmaev.domain.mapper.AttributeMapper;
import ru.zmaev.domain.mapper.CharacterMapper;
import ru.zmaev.domain.mapper.StartClassMapper;
import ru.zmaev.repository.AttributeRepository;
import ru.zmaev.repository.CharacterRepository;
import ru.zmaev.repository.StartClassRepository;
import ru.zmaev.service.BuildService;
import ru.zmaev.service.CharacterService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final AttributeRepository attributeRepository;
    private final StartClassRepository startClassRepository;

    private final CharacterMapper characterMapper;
    private final AttributeMapper attributeMapper;
    private final StartClassMapper startClassMapper;

    private final BuildService buildService;

    private final UserInfo userInfo;

    @Override
    @Transactional(readOnly = true)
    public Page<CharacterResponseDto> findAll(Pageable pageable) {
        log.info("Fetching all builds with pagination: {}", pageable);
        Page<Character> characters = characterRepository.findAll(pageable);
        return characters.map(characterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StartClassResponseDto> findAllStartClass() {
        log.info("Fetching all start classes");
        List<StartClass> startClasses = startClassRepository.findAll();
        return startClasses.stream().map(startClassMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterResponseDto findById(Long id) {
        log.info("Fetching character by id: {}", id);
        Character character = characterRepository.findById(id).orElseThrow(() ->
                new EntityNotFountException("Character", id));
        return characterMapper.toDto(character);
    }

    @Override
    @Transactional
    public CharacterResponseDto create(CharacterCreateRequestDto requestDto) {
        Build build = buildService.getBuildOrThrow(requestDto.getBuildId());
        StartClass startClass = getStartClassOrThrow(requestDto.getStartClassId());
        if (!compareAttributeFields(requestDto.getAttributes(), startClass.getAttribute())) {
            throw new EntityBadRequestException("Request attribute fields can`t be less than StartClass attributes");
        }
        log.info("Creating new character and attribute with request: {}", requestDto);
        Character character = characterMapper.toEntity(requestDto);
        Attribute attribute = attributeMapper.toEntity(requestDto.getAttributes());
        attributeRepository.save(attribute);
        log.info("Attribute created successfully with id: {}", attribute.getId());
        character.setAttribute(attribute);
        character.setBuild(build);
        character.setStartClass(startClass);
        characterRepository.save(character);
        log.info("Character created successfully with id: {}", character.getId());
        return characterMapper.toDto(character);
    }

    @Override
    @Transactional
    public CharacterResponseDto update(CharacterUpdateRequestDto requestDto) {
        log.info("Update character with id: {}", requestDto.getCharacterId());
        StartClass startClass = getStartClassOrThrow(requestDto.getStartClassId());
        if (!compareAttributeFields(requestDto.getAttributes(), startClass.getAttribute())) {
            throw new EntityBadRequestException("Request attribute fields can`t be less than StartClass attributes");
        }
        Character character = getCharacterOrThrow(requestDto.getCharacterId());
        Build build = character.getBuild();
        if (!Objects.equals(build.getUser().getId(), userInfo.getUserId())) {
            throw new NoAccessException(userInfo.getUsername(), userInfo.getRole().toString());
        }
        Attribute attribute = attributeMapper.toEntity(requestDto.getAttributes());
        Attribute savedAttribute = attributeRepository.save(attribute);
        log.info("Attributes updated successfully with id: {}", userInfo.getUserId());
        character = characterMapper.toEntity(requestDto);
        character.setAttribute(savedAttribute);
        character.setBuild(build);
        character.setStartClass(startClass);
        character = characterRepository.save(character);
        log.info("Character updated successfully with id: {}", userInfo.getUserId());
        return characterMapper.toDto(character);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Character character = getCharacterOrThrow(id);
        if (!Objects.equals(character.getBuild().getUser().getId(), userInfo.getUserId()) &&
                userInfo.getRole().contains(Role.ROLE_ADMIN.getKeycloakRoleName())) {
            throw new NoAccessException(userInfo.getUsername(), userInfo.getRole().toString());
        }
        log.info("Deleting character with id: {}", id);
        characterRepository.deleteById(id);
        log.info("Character deleted successfully with id: {}", id);
    }

    private Character getCharacterOrThrow(Long id) {
        return characterRepository.findById(id).orElseThrow(() ->
                new EntityNotFountException("Character", id));
    }

    private StartClass getStartClassOrThrow(Long requestDto) {
        return startClassRepository.findById(requestDto).orElseThrow(() ->
                new EntityNotFountException("StartClass", requestDto));
    }

    private boolean compareAttributeFields(AttributeCreateRequestDto dto, Attribute attribute) {
        return (dto.getVigor() == null || (dto.getVigor() >= attribute.getVigor() && dto.getVigor() <= 99))
                && (dto.getMind() == null || (dto.getMind() >= attribute.getMind() && dto.getMind() <= 99))
                && (dto.getEndurance() == null || (dto.getEndurance() >= attribute.getEndurance() && dto.getEndurance() <= 99))
                && (dto.getStrength() == null || (dto.getStrength() >= attribute.getStrength() && dto.getStrength() <= 99))
                && (dto.getDexterity() == null || (dto.getDexterity() >= attribute.getDexterity() && dto.getDexterity() <= 99))
                && (dto.getIntelligence() == null || (dto.getIntelligence() >= attribute.getIntelligence() && dto.getIntelligence() <= 99))
                && (dto.getFaith() == null || (dto.getFaith() >= attribute.getFaith() && dto.getFaith() <= 99))
                && (dto.getArcana() == null || (dto.getArcana() >= attribute.getArcana() && dto.getArcana() <= 99));
    }
}
