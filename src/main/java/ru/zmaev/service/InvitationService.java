package ru.zmaev.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.domain.dto.request.InvitationCreateRequestDto;
import ru.zmaev.domain.dto.response.InvitationResponseDto;

import java.util.List;

public interface InvitationService {
    Page<InvitationResponseDto> findAll(Pageable pageable);
    InvitationResponseDto findById(Long id);

    @Transactional
    InvitationResponseDto addUsersToInvitation(Long invitationId, List<String> userIds);

    InvitationResponseDto save(InvitationCreateRequestDto requestDto);
    void delete(Long id);

    @Transactional
    void deleteUser(Long id, String userId);
}
