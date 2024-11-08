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
import ru.zmaev.domain.dto.request.AshesOfWarCreateRequestDto;
import ru.zmaev.domain.dto.request.AshesOfWarUpdateRequestDto;
import ru.zmaev.domain.dto.response.AshesOfWarResponseDto;

@Tag(name = "AshesOfWar API", description = "API для работы с сущностью AshesOfWar")
public interface AshesOfWarApi {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка пеплов войны",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AshesOfWarResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка пеплов войны",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Page<AshesOfWarResponseDto>> findAll(
            @Parameter(name = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(name = "Размер страницы") @Min(1) Integer pageSize);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение пепла войны",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AshesOfWarResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пепла войны по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение пепла войны",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<AshesOfWarResponseDto> findById(@Parameter(description = "id пепла войны") Long id);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное создание пепла войны",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AshesOfWarResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Нет прав доступа",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Сохранение нового пепла войны",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<AshesOfWarResponseDto> create(@Valid AshesOfWarCreateRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление пепла войны",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AshesOfWarResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пепла войны по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Нет прав доступа",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Обновление пепла войны",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<AshesOfWarResponseDto> update(@Valid AshesOfWarUpdateRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление пепла войны"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пепла войны по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Нет прав доступа",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Удаление пепла войны",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<Void> delete(@Parameter(description = "id пепла войны") Long id);
}
