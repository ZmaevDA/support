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
import ru.zmaev.domain.dto.request.AshesOfWarCreateRequestDto;
import ru.zmaev.domain.dto.request.AshesOfWarUpdateRequestDto;
import ru.zmaev.domain.dto.response.AshesOfWarResponseDto;
import ru.zmaev.domain.entity.AshesOfWar;
import ru.zmaev.domain.mapper.AshesOfWarMapper;
import ru.zmaev.repository.AshesOfWarRepository;
import ru.zmaev.service.AshesOfWarService;
import ru.zmaev.service.impl.AshesOfWarServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AshesOfWarServiceTests {

    @Mock
    private AshesOfWarRepository ashesOfWarRepository;

    @Mock
    private AshesOfWarMapper ashesOfWarMapper;

    private AshesOfWarService ashesOfWarService;
    private AshesOfWar ashesOfWar;
    private AshesOfWarResponseDto ashesOfWarResponseDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ashesOfWar = new AshesOfWar();
        ashesOfWar.setId(1L);

        ashesOfWarResponseDto = new AshesOfWarResponseDto();
        ashesOfWarResponseDto.setId(1L);

        ashesOfWarService = new AshesOfWarServiceImpl(
                ashesOfWarRepository,
                ashesOfWarMapper);
    }

    @Test
    void findAll() {
        Pageable pageable = mock(Pageable.class);
        List<AshesOfWar> ashesOfWarList = List.of(new AshesOfWar(), new AshesOfWar());
        Page<AshesOfWar> ashesOfWarPage = new PageImpl<>(ashesOfWarList);
        when(ashesOfWarRepository.findAll(pageable)).thenReturn(ashesOfWarPage);
        when(ashesOfWarMapper.toDto(any(AshesOfWar.class)))
                .thenReturn(new AshesOfWarResponseDto());

        Page<AshesOfWarResponseDto> result = ashesOfWarService.findAll(pageable);
        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void findById() {
        when(ashesOfWarRepository.findById(1L)).thenReturn(Optional.of(ashesOfWar));
        when(ashesOfWarMapper.toDto(ashesOfWar)).thenReturn(ashesOfWarResponseDto);
        AshesOfWarResponseDto responseDto = ashesOfWarService.findById(1L);
        Assertions.assertThat(responseDto).isNotNull().isEqualTo(ashesOfWarResponseDto);
    }

    @Test
    void create() {
        AshesOfWarCreateRequestDto requestDto = new AshesOfWarCreateRequestDto();
        requestDto.setName("AshOfWar");
        requestDto.setDescription("AshOfWar Description");

        when(ashesOfWarMapper.toEntity(requestDto)).thenReturn(ashesOfWar);
        when(ashesOfWarRepository.save(any(AshesOfWar.class))).thenAnswer(invocation -> {
            AshesOfWar savedAshesOfWar = invocation.getArgument(0);
            savedAshesOfWar.setId(1L);
            return savedAshesOfWar;
        });

        when(ashesOfWarMapper.toDto(ashesOfWar)).thenReturn(ashesOfWarResponseDto);

        AshesOfWarResponseDto result = ashesOfWarService.create(requestDto);

        Assertions.assertThat(result).isNotNull().isEqualTo(ashesOfWarResponseDto);
    }

    @Test
    void update() {
        AshesOfWarUpdateRequestDto requestDto = new AshesOfWarUpdateRequestDto();
        requestDto.setId(1L);
        requestDto.setName("Updated AshOfWar");
        requestDto.setDescription("Updated AshOfWar Description");
        when(ashesOfWarRepository.existsById(1L)).thenReturn(true);
        when(ashesOfWarMapper.toEntity(requestDto)).thenReturn(ashesOfWar);
        when(ashesOfWarRepository.save(any(AshesOfWar.class))).thenAnswer(invocation -> {
            AshesOfWar savedAshesOfWar = invocation.getArgument(0);
            savedAshesOfWar.setId(1L);
            return savedAshesOfWar;
        });

        when(ashesOfWarMapper.toDto(ashesOfWar)).thenReturn(ashesOfWarResponseDto);

        AshesOfWarResponseDto result = ashesOfWarService.update(requestDto);

        Assertions.assertThat(result).isNotNull().isEqualTo(ashesOfWarResponseDto);
    }

    @Test
    void delete() {

        when(ashesOfWarRepository.findById(1L)).thenReturn(Optional.of(ashesOfWar));

        ashesOfWarService.delete(1L);

        verify(ashesOfWarRepository, times(1)).delete(ashesOfWar);
    }
}
