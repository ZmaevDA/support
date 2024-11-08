package ru.zmaev.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.zmaev.controller.api.InvitationApi;
import ru.zmaev.domain.dto.request.InvitationCreateRequestDto;
import ru.zmaev.domain.dto.response.InvitationResponseDto;
import ru.zmaev.service.InvitationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/invitations")
public class InvitationController implements InvitationApi {

    private final InvitationService invitationService;

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<InvitationResponseDto>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer pagePosition,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize) {
        Page<InvitationResponseDto> invitations = invitationService.findAll(PageRequest.of(pagePosition, pageSize));
        return ResponseEntity.ok(invitations);
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InvitationResponseDto> findById(@PathVariable Long id) {
        InvitationResponseDto invitation = invitationService.findById(id);
        return ResponseEntity.ok(invitation);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ROLE_EDITOR, ROLE_ADMIN')")
    public ResponseEntity<InvitationResponseDto> create(@RequestBody InvitationCreateRequestDto requestDto) {

        InvitationResponseDto invitation = invitationService.save(requestDto);
        return ResponseEntity.ok(invitation);
    }

    @PatchMapping("{id}/users")
    @PreAuthorize("hasRole('ROLE_EDITOR, ROLE_ADMIN')")
    public ResponseEntity<InvitationResponseDto> addUsers(@PathVariable Long id, @RequestBody List<String> userIds) {
        InvitationResponseDto invitation = invitationService.addUsersToInvitation(id, userIds);
        return ResponseEntity.ok(invitation);
    }

    @DeleteMapping("{invitationId}/users/{userId}")
    @PreAuthorize("hasRole('ROLE_EDITOR, ROLE_ADMIN')")
    public ResponseEntity<InvitationResponseDto> deleteUsers(@PathVariable Long invitationId, @PathVariable String userId) {
        invitationService.deleteUser(invitationId, userId);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EDITOR, ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        invitationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
