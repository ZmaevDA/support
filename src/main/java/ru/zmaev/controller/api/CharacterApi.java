package ru.zmaev.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.zmaev.commonlib.exception.message.ErrorMessage;
import ru.zmaev.domain.dto.request.CharacterCreateRequestDto;
import ru.zmaev.domain.dto.request.CharacterUpdateRequestDto;
import ru.zmaev.domain.dto.response.CharacterResponseDto;
import ru.zmaev.domain.dto.response.StartClassResponseDto;

import java.util.List;

@Tag(name = "Character API", description = "API для работы с сущностью Character")
public interface CharacterApi {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка персонажей и их атрибутов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CharacterResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка персонажей и их атрибутов",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Page<CharacterResponseDto>> findAll(
            @Parameter(name = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(name = "Размер страницы") @Min(1) Integer pageSize);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка начальных классов и их атрибутов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CharacterResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка начальных классов и их атрибутов",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<List<StartClassResponseDto>> findAllStartClasses();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение персонажа и его атрибутов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CharacterResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Персонажа по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение персонажа и его атрибутов по id",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<CharacterResponseDto> findById(@Parameter(description = "id персонажа") Long id);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное создание персонажа и его атрибутов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CharacterResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Сохранение нового персонажа и его атрибутов",
            description = "Access: ROLE_EDITOR"
    )
    ResponseEntity<CharacterResponseDto> create(@Valid CharacterCreateRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление персонажа и его атрибутов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CharacterResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Персонажа по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Обновление персонажа и его атрибутов",
            description = "Access: ROLE_EDITOR"
    )
    ResponseEntity<CharacterResponseDto> update(@Valid CharacterUpdateRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление персонажа и его атрибутов"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Персонажа по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Удаление персонажа и его атрибутов",
            description = "Access: ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Void> delete(@Parameter(description = "id персонажа") Long id);
}
