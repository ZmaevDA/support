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
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import ru.zmaev.commonlib.exception.message.ErrorMessage;
import ru.zmaev.commonlib.model.dto.response.EntityIsExistsResponseDto;
import ru.zmaev.domain.dto.request.BuildCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildUpdateRequestDto;
import ru.zmaev.domain.dto.response.BuildDetailedResponseDto;
import ru.zmaev.domain.dto.response.BuildResponseDto;
import ru.zmaev.domain.dto.response.CommentResponseDto;
import ru.zmaev.domain.enums.ReactionType;

@Tag(name = "Build API", description = "API для работы с сущностью Build")
public interface BuildApi {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка публичных сборок",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BuildResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка публичных сборок",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Page<BuildResponseDto>> findAllPublic(
            @Parameter(description = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(description = "Размер страницы") @Min(1) Integer pageSize,
            @Parameter(description = "Направление сортировки") Sort.Direction sortDirection);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка сборок",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BuildResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка сборок",
            description = "Access: ROLE_ADMIN"
    )
    ResponseEntity<Page<BuildResponseDto>> findAll(
            @Parameter(description = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(description = "Размер страницы") @Min(1) Integer pageSize,
            @Parameter(description = "Направление сортировки") Sort.Direction sortDirection);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка сборок",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BuildResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка сборок elasticsearch",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Page<BuildResponseDto>> findAllSearch(
            @Parameter(description = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(description = "Размер страницы") @Min(1) Integer pageSize,
            @Parameter(description = "Поиск") @Size(min = 3) String search);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка приватных сборок, доступ к которыместь у пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BuildResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка приватных сборок пользователя",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Page<BuildResponseDto>> findAllUsersAllowed(
            @Parameter(description = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(description = "Размер страницы") @Min(1) Integer pageSize,
            @Parameter(description = "Id пользователя") String userId
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение сборки",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BuildResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сборки по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение сборки",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<BuildResponseDto> findById(
            @Parameter(description = "id сборки") Long id,
            @Parameter(description = "токен доступа") String token
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное создание сборки",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BuildResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Сохранение новой сборки",
            description = "Access: ROLE_EDITOR"
    )
    ResponseEntity<BuildResponseDto> create(@Valid BuildCreateRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление сборки",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BuildResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сборки по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Обновление cборки",
            description = "Access: ROLE_EDITOR"
    )
    ResponseEntity<BuildResponseDto> update(@Valid BuildUpdateRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление сборки"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сборки по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Удаление сборки",
            description = "Access: ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Void> delete(@Parameter(description = "id сборки") Long id);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение детализированной информации о сборке",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BuildDetailedResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сборки по переданному id нет",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение детализированной информации о сборке",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<BuildDetailedResponseDto> findByIdDetailed(
            @Parameter(description = "id сборки") Long id,
            @Parameter(description = "токен доступа") String token);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение статуса существования сборки",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EntityIsExistsResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Проверка существования сборки по id",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<EntityIsExistsResponseDto> isExistById(@Parameter(description = "id сборки") Long id);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение списка коментариев существования сборки",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EntityIsExistsResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Проверка существования сборки по id",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Page<CommentResponseDto>> findAllCommentsByBuild(
            @Parameter(description = "Id сборки") Long id,
            @Parameter(description = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(description = "Размер страницы") @Min(1) Integer pageSize,
            @Parameter(description = "Направление сортировки") Sort.Direction sortDirection
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Успешное добавление комментария к сборке",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EntityIsExistsResponseDto.class)
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сборка с указанным Id не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @Operation(
            summary = "Добавление коммантария к сборке по id",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Void> addComment(
            @Parameter(description = "Id сборки") Long id,
            @Parameter(description = "Id пользователя") String userId,
            @Parameter(description = "Текст комментария") String content
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Реакция успешно добавлена"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры запроса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сборка с указанным Id не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @Operation(
            summary = "Добавление реакции к сборке",
            description = "ACCESS: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Void> addReaction(
            @Parameter(description = "Id сборки", required = true) Long id,
            @Parameter(description = "Id пользователя", required = true) String userId,
            @Parameter(description = "Тип реакции", required = true, schema = @Schema(implementation = ReactionType.class))
            ReactionType reactionType
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Реакция успешно удалена"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры запроса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сборка с указанным Id не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @Operation(
            summary = "Удаление реакции у сборки",
            description = "ACCESS: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Void> deleteReaction(
            @Parameter(description = "Id реакции", required = true) Long id
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Успешное удаления комментария к сборке",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EntityIsExistsResponseDto.class)
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сборка с указанным Id не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @Operation(
            summary = "Удаление комментария по id",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Void> deleteComment(
            @Parameter(description = "Id сборки") Long id
    );
}
