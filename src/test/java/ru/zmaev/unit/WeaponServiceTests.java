package ru.zmaev.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.zmaev.domain.dto.request.WeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.WeaponUpdateRequestDto;
import ru.zmaev.domain.dto.response.WeaponResponseDto;
import ru.zmaev.domain.entity.Attribute;
import ru.zmaev.domain.entity.Weapon;
import ru.zmaev.domain.mapper.AttributeMapper;
import ru.zmaev.domain.mapper.WeaponMapper;
import ru.zmaev.repository.AttributeRepository;
import ru.zmaev.repository.WeaponRepository;
import ru.zmaev.service.impl.WeaponServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeaponServiceTests {

    @Mock
    private WeaponRepository weaponRepository;

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private WeaponMapper weaponMapper;

    @Mock
    private AttributeMapper attributeMapper;

    private WeaponServiceImpl weaponService;

    private Weapon weapon;
    private WeaponResponseDto weaponResponseDto;

    @BeforeEach
    public void setup() {
        weapon = new Weapon();
        weapon.setId(1L);
        weapon.setName("Weapon");

        weaponResponseDto = new WeaponResponseDto();
        weaponResponseDto.setId(1L);
        weaponResponseDto.setName("Weapon");
        weaponService = new WeaponServiceImpl(
                weaponRepository,
                attributeRepository,
                weaponMapper,
                attributeMapper
        );
    }

    @Test
    void findAll() {
        Pageable pageable = mock(Pageable.class);
        List<Weapon> weapons = List.of(new Weapon(), new Weapon());
        Page<Weapon> weaponPage = new PageImpl<>(weapons);

        when(weaponRepository.findAll(pageable))
                .thenReturn(weaponPage);
        when(weaponMapper.toDto(any(Weapon.class)))
                .thenReturn(new WeaponResponseDto());


        Page<WeaponResponseDto> result = weaponService.findAll(pageable);
        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void findById() {
        when(weaponRepository.findById(1L)).thenReturn(Optional.of(weapon));
        when(weaponMapper.toDto(weapon)).thenReturn(weaponResponseDto);

        WeaponResponseDto responseDto = weaponService.findById(1L);
        Assertions.assertThat(responseDto).isNotNull().isEqualTo(weaponResponseDto);
    }

    @Test
    void create() {
        WeaponCreateRequestDto requestDto = new WeaponCreateRequestDto();
        Weapon weapon = new Weapon();
        Attribute attribute = new Attribute();
        WeaponResponseDto responseDto = new WeaponResponseDto();

        when(weaponMapper.toEntity(requestDto)).thenReturn(weapon);
        when(attributeMapper.toEntity(requestDto)).thenReturn(attribute);
        when(attributeRepository.save(attribute)).thenReturn(attribute);
        when(weaponRepository.save(weapon)).thenReturn(weapon);
        when(weaponMapper.toDto(weapon)).thenReturn(responseDto);

        WeaponResponseDto result = weaponService.create(requestDto);

        Assertions.assertThat(result).isNotNull().isEqualTo(responseDto);
    }

    @Test
    void update() {
        WeaponUpdateRequestDto requestDto = new WeaponUpdateRequestDto();
        requestDto.setId(1L);
        requestDto.setName("Updated Weapon");
        requestDto.setDescription("Updated Weapon Description");
        when(weaponRepository.findById(anyLong())).thenReturn(Optional.of(weapon));
        when(weaponMapper.toEntity(any(WeaponUpdateRequestDto.class))).thenReturn(weapon);
        when(weaponRepository.save(any(Weapon.class))).thenReturn(weapon);
        when(weaponMapper.toDto(any(Weapon.class))).thenReturn(weaponResponseDto);

        WeaponResponseDto result = weaponService.update(requestDto);

        Assertions.assertThat(result).isNotNull().isEqualTo(weaponResponseDto);
    }

    @Test
    void delete() {
        Weapon weapon = new Weapon();

        given(weaponRepository.findById(1L)).willReturn(Optional.of(weapon));

        weaponService.delete(1L);

        verify(weaponRepository).findById(1L);
        verify(weaponRepository).delete(weapon);
    }
}