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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.zmaev.commonlib.exception.message.ErrorMessage;
import ru.zmaev.domain.dto.request.InventoryItemCreateRequestDto;
import ru.zmaev.domain.dto.request.InventoryItemUpdateRequestDto;
import ru.zmaev.domain.dto.response.InventoryItemResponseDto;

@Tag(name = "InventoryItem API", description = "API для работы с сущностью InventoryItem")
public interface InventoryItemApi {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка предметов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = InventoryItemResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка предметов",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Page<InventoryItemResponseDto>> findAll(
            @Parameter(name = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(name = "Размер страницы") @Min(1) Integer pageSize);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение предмета",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = InventoryItemResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Предмета по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение предмета",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<InventoryItemResponseDto> findById(@Parameter(description = "id предмета") Long id);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное создание предмета",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = InventoryItemResponseDto.class)
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
            summary = "Сохранение нового предмета",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<InventoryItemResponseDto> create(@Valid InventoryItemCreateRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление предмета",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = InventoryItemResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Предмета по переданному id нет",
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
            summary = "Обновление предмета",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<InventoryItemResponseDto> update(@Valid InventoryItemUpdateRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление предмета"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Предмета по переданному id нет",
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
            summary = "Удаление предмета",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<Void> delete(@Parameter(description = "id предмета") Long id);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Документ успешно загружен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Невозможно загрузить документ",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Загрузить изображение"
    )
    ResponseEntity<InventoryItemResponseDto> uploadImage(
            @Parameter(description = "Id предмета", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Изображение")
            @RequestParam("file") MultipartFile file);
}
