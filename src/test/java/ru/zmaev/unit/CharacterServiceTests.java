package ru.zmaev.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.zmaev.commonlib.model.dto.UserInfo;
import ru.zmaev.domain.dto.request.AttributeCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterUpdateRequestDto;
import ru.zmaev.domain.dto.response.CharacterResponseDto;
import ru.zmaev.domain.entity.Character;
import ru.zmaev.domain.entity.*;
import ru.zmaev.domain.mapper.AttributeMapper;
import ru.zmaev.domain.mapper.CharacterMapper;
import ru.zmaev.domain.mapper.StartClassMapper;
import ru.zmaev.repository.AttributeRepository;
import ru.zmaev.repository.CharacterRepository;
import ru.zmaev.repository.StartClassRepository;
import ru.zmaev.service.BuildService;
import ru.zmaev.service.CharacterService;
import ru.zmaev.service.impl.CharacterServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTests {

    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private StartClassRepository startClassRepository;
    @Mock
    private CharacterMapper characterMapper;
    @Mock
    private AttributeMapper attributeMapper;
    @Mock
    private StartClassMapper startClassMapper;
    @Mock
    private BuildService buildService;
    @Mock
    private UserInfo userInfo;
    private CharacterService characterService;
    private User user;
    private Character character;
    private CharacterResponseDto characterResponseDto;
    private Attribute attribute;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("1L");
        user.setUsername("testUser");

        userInfo.setUserId("1L");

        Build build = new Build();
        build.setId(1L);
        build.setUser(user);

        attribute = new Attribute();
        attribute.setId(11L);
        attribute.setVigor(20);
        attribute.setMind(20);
        attribute.setEndurance(20);
        attribute.setStrength(20);
        attribute.setDexterity(20);
        attribute.setIntelligence(20);
        attribute.setFaith(20);
        attribute.setArcana(20);

        character = new Character();
        character.setId(1L);
        character.setAttribute(attribute);
        character.setBuild(build);

        characterResponseDto = new CharacterResponseDto();
        characterResponseDto.setId(1L);

        characterService = new CharacterServiceImpl(
                characterRepository,
                attributeRepository,
                startClassRepository,
                characterMapper,
                attributeMapper,
                startClassMapper,
                buildService,
                userInfo);
    }

    @Test
    void findAll() {
        Pageable pageable = mock(Pageable.class);
        List<Character> characters = List.of(new Character(), new Character());
        Page<Character> characterPage = new PageImpl<>(characters);
        when(characterRepository.findAll(pageable)).thenReturn(characterPage);
        when(characterMapper.toDto(any(Character.class)))
                .thenReturn(new CharacterResponseDto());

        Page<CharacterResponseDto> result = characterService.findAll(pageable);
        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void findById() {
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(characterMapper.toDto(character)).thenReturn(characterResponseDto);
        CharacterResponseDto responseDto = characterService.findById(1L);
        Assertions.assertThat(responseDto).isNotNull().isEqualTo(characterResponseDto);
    }

    @Test
    void create() {
        CharacterCreateRequestDto requestDto = new CharacterCreateRequestDto();
        requestDto.setBuildId(1L);
        requestDto.setAttributes(new AttributeCreateRequestDto(20, 20, 20, 20, 20, 20, 20, 20));
        requestDto.setStartClassId(1L);

        StartClass startClass = new StartClass();
        startClass.setAttribute(attribute);
        startClass.setId(1L);

        Build build = new Build();
        build.setId(1L);

        Character createdCharacter = new Character();
        createdCharacter.setId(1L);
        createdCharacter.setAttribute(attribute);
        createdCharacter.setBuild(build);
        createdCharacter.setStartClass(startClass);
        build.setCharacters(Set.of(createdCharacter));

        when(startClassRepository.findById(anyLong())).thenReturn(Optional.of(startClass));
        when(buildService.getBuildOrThrow(anyLong())).thenReturn(build);
        when(attributeMapper.toEntity(any(AttributeCreateRequestDto.class))).thenReturn(attribute);
        when(characterMapper.toEntity(any(CharacterCreateRequestDto.class))).thenReturn(createdCharacter);
        when(attributeRepository.save(any(Attribute.class))).thenAnswer(invocation -> {
            Attribute savedAttribute = invocation.getArgument(0);
            savedAttribute.setId(11L);
            savedAttribute.setVigor(20);
            savedAttribute.setMind(20);
            savedAttribute.setEndurance(20);
            savedAttribute.setStrength(20);
            savedAttribute.setDexterity(20);
            savedAttribute.setIntelligence(20);
            savedAttribute.setFaith(20);
            savedAttribute.setArcana(20);
            return savedAttribute;
        });
        when(characterRepository.save(any(Character.class))).thenAnswer(invocation -> {
            Character savedCharacter = invocation.getArgument(0);
            savedCharacter.setId(1L);
            savedCharacter.setAttribute(attribute);
            return savedCharacter;
        });

        when(characterMapper.toDto(createdCharacter)).thenReturn(characterResponseDto);

        CharacterResponseDto result = characterService.create(requestDto);

        Assertions.assertThat(result).isNotNull().isEqualTo(characterResponseDto);
    }

    @Test
    void update() {
        Character updatedCharacter = new Character();
        updatedCharacter.setId(1L);
        updatedCharacter.setAttribute(attribute);

        CharacterUpdateRequestDto requestDto = new CharacterUpdateRequestDto();
        requestDto.setAttributes(new AttributeCreateRequestDto(20, 20, 20, 20, 20, 20, 20, 20));
        requestDto.setStartClassId(1L);
        requestDto.setCharacterId(1L);

        StartClass startClass = new StartClass();
        startClass.setId(1L);
        startClass.setAttribute(attribute);

        Build build = new Build();
        build.setId(1L);
        build.setUser(user);

        Character createdCharacter = new Character();
        createdCharacter.setId(1L);
        createdCharacter.setAttribute(attribute);
        createdCharacter.setBuild(build);
        createdCharacter.setStartClass(startClass);
        build.setCharacters(Set.of(createdCharacter));

        when(userInfo.getUserId()).thenReturn("1L");
        when(startClassRepository.findById(anyLong())).thenReturn(Optional.of(startClass));
        when(characterRepository.findById(anyLong())).thenReturn(Optional.of(character));
        when(attributeMapper.toEntity(any(AttributeCreateRequestDto.class))).thenReturn(attribute);
        when(characterMapper.toEntity(any(CharacterUpdateRequestDto.class))).thenReturn(updatedCharacter);
        when(attributeRepository.save(any(Attribute.class))).thenAnswer(invocation -> {
            Attribute savedAttribute = invocation.getArgument(0);
            savedAttribute.setId(1L);
            return savedAttribute;
        });
        when(characterRepository.save(any(Character.class))).thenAnswer(invocation -> {
            Character savedCharacter = invocation.getArgument(0);
            savedCharacter.setId(1L);
            return savedCharacter;
        });

        when(characterMapper.toDto(updatedCharacter)).thenReturn(characterResponseDto);

        CharacterResponseDto result = characterService.update(requestDto);

        Assertions.assertThat(result).isNotNull().isEqualTo(characterResponseDto);
    }

    @Test
    void delete() {
        when(userInfo.getUserId()).thenReturn("1L");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));

        characterService.delete(1L);

        verify(characterRepository, times(1)).deleteById(1L);
    }
}
