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
import ru.zmaev.domain.dto.request.InvitationCreateRequestDto;
import ru.zmaev.domain.dto.response.InvitationResponseDto;

@Tag(name = "Invitation API", description = "API для работы с сущностью Invitation")
public interface InvitationApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный возврат списка приглашений",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = InvitationResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение списка приглашений",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Page<InvitationResponseDto>> findAll(
            @Parameter(name = "Начальная страница") @Min(0) Integer pagePosition,
            @Parameter(name = "Размер страницы") @Min(1) Integer pageSize);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение приглашения",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = InvitationResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Приглашение по переданному id не найдено",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Получение приглашения",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<InvitationResponseDto> findById(@Parameter(description = "id приглашения") Long id);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное создание приглашения",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = InvitationResponseDto.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Создание нового приглашения",
            description = "Access: ROLE_EDITOR"
    )
    ResponseEntity<InvitationResponseDto> create(@Valid InvitationCreateRequestDto requestDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление приглашения",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Приглашение по переданному id не найдено",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Удаление приглашения",
            description = "Access: ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<Void> delete(@Parameter(description = "id приглашения") Long id);
}

