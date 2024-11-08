package ru.zmaev.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.zmaev.commonlib.exception.message.ErrorMessage;
import ru.zmaev.domain.dto.request.WeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.WeaponUpdateRequestDto;
import ru.zmaev.domain.dto.response.WeaponResponseDto;

@Tag(name = "Weapon API", description = "API для работы с сущностью Weapon")
public interface WeaponApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка с оружием",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WeaponResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка с оружием",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Page<WeaponResponseDto>> findAll(
            @Parameter(name = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(name = "Размер страницы") @Min(1) Integer pageSize
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение оружия по id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WeaponResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Оружия по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение орижия по id",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<WeaponResponseDto> findById(@Parameter(description = "id оружия") Long id);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное создание оружия",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WeaponResponseDto.class)
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
            summary = "Сохранение нового оружия",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<WeaponResponseDto> create(WeaponCreateRequestDto weaponCreateRequestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление оружия",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WeaponResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Оружия по переданному id нет",
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
            summary = "Обновление оружия",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<WeaponResponseDto> update(WeaponUpdateRequestDto weaponRequestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление оружия",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WeaponResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Оружия по переданному id нет",
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
            summary = "Удаление оружия",
            description = "Access: ROLE_ADMIN"
    )
    void delete(@Parameter(description = "id оружия") Long id);
}
