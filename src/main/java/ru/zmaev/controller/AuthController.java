package ru.zmaev.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zmaev.auth.AuthService;
import ru.zmaev.commonlib.model.dto.response.UserFullResponseDto;
import ru.zmaev.controller.api.AuthApi;
import ru.zmaev.domain.dto.request.UserKeycloakDataDto;

@Slf4j
@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;


    @Value("${secret-key}")
    private String secretKey;

    @PostMapping("/register")
    public ResponseEntity<UserFullResponseDto> register(@RequestBody UserKeycloakDataDto dto,
                                                        @RequestHeader("X-Secret-Key") String secretKey) {
        log.info("SECRET_KEY loaded: {}", this.secretKey);
        if (!this.secretKey.equals(secretKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(authService.register(dto));
    }
}
