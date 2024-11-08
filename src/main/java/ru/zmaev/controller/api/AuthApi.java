package ru.zmaev.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.zmaev.commonlib.exception.message.ErrorMessage;
import ru.zmaev.commonlib.model.dto.response.UserFullResponseDto;
import ru.zmaev.domain.dto.request.UserKeycloakDataDto;

@Tag(name = "Auth API", description = "API для работы с авторизацией")
public interface AuthApi {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешная регистрация",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserFullResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Пользователь уже существует в системе",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = "Регистрация в системе",
            description = "Access: ROLE_USER, ROLE_EDITOR, ROLE_ADMIN"
    )
    ResponseEntity<UserFullResponseDto> register(UserKeycloakDataDto dto, String secretKey);
}
