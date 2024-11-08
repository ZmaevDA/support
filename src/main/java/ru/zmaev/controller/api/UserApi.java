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
import org.springframework.web.bind.annotation.RequestHeader;
import ru.zmaev.commonlib.exception.message.ErrorMessage;
import ru.zmaev.commonlib.model.dto.common.UserCommonResponseDto;
import ru.zmaev.commonlib.model.dto.response.EntityIsExistsResponseDto;
import ru.zmaev.commonlib.model.dto.response.UserFullResponseDto;
import ru.zmaev.domain.dto.request.UserUpdateRequestDto;

@Tag(name = "User API", description = "API для работы с пользователями")
public interface UserApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка пользователей",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserFullResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка пользователей",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<Page<UserFullResponseDto>> findAll(
            @Parameter(description = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(description = "Размер страницы") @Min(1) Integer pageSize
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserFullResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь по переданному id не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение пользователя",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<UserCommonResponseDto> findById(@Parameter(description = "id пользователя") String id,
                                                   @RequestHeader(value = "dtoType", required = false) String dtoType);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешная проверка на существование пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EntityIsExistsResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Проверка существования пользователя",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<EntityIsExistsResponseDto> existsById(@PathVariable String uuid);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение информации о текущем пользователе",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserFullResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение информации о текущем пользователе",
            description = "Access: Все пользователи"
    )
    ResponseEntity<UserFullResponseDto> getUserInfo();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserFullResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь по переданному id не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Обновление пользователя",
            description = "Access: Все пользователи"
    )
    ResponseEntity<UserFullResponseDto> update(
            @Parameter(description = "uuid пользователя") String uuid,
            @Valid UserUpdateRequestDto requestDto
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserFullResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь по переданному id не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Удаление пользователя",
            description = "Access: Все пользователи"
    )
    ResponseEntity<UserFullResponseDto> delete(@Parameter(description = "uuid пользователя") String uuid);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное восстановление пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserFullResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь по переданному id не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Восстановление пользователя",
            description = "Access: Все пользователи"
    )
    ResponseEntity<UserFullResponseDto> recover(@Parameter(description = "uuid пользователя") String uuid);
}
