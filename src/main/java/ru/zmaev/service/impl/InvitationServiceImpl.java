package ru.zmaev.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.commonlib.exception.EntityConflictException;
import ru.zmaev.commonlib.exception.EntityNotFountException;
import ru.zmaev.commonlib.exception.NoAccessException;
import ru.zmaev.commonlib.model.dto.UserInfo;
import ru.zmaev.commonlib.model.enums.Role;
import ru.zmaev.domain.dto.request.InvitationCreateRequestDto;
import ru.zmaev.domain.dto.response.InvitationResponseDto;
import ru.zmaev.domain.entity.Build;
import ru.zmaev.domain.entity.Invitation;
import ru.zmaev.domain.entity.InvitationPrincipal;
import ru.zmaev.domain.entity.User;
import ru.zmaev.domain.mapper.InvitationMapper;
import ru.zmaev.repository.InvitationPrincipalRepository;
import ru.zmaev.repository.InvitationRepository;
import ru.zmaev.service.BuildService;
import ru.zmaev.service.InvitationService;
import ru.zmaev.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final InvitationPrincipalRepository invitationPrincipalRepository;

    private final InvitationMapper invitationMapper;

    private final BuildService buildService;
    private final UserService userService;

    private final UserInfo userInfo;

    @Override
    public Page<InvitationResponseDto> findAll(Pageable pageable) {
        Page<Invitation> invitations = invitationRepository.findAll(pageable);
        return invitations.map(invitationMapper::toDto);
    }

    @Override
    public InvitationResponseDto findById(Long id) {
        Invitation invitation = getInvitationOrThrow(id);
        return invitationMapper.toDto(invitation);
    }

    @Override
    @Transactional
    public InvitationResponseDto addUsersToInvitation(Long invitationId, List<String> userIds) {
        Invitation invitation = getInvitationOrThrow(invitationId);
        Build build = invitation.getBuild();
        List<User> users = validateUsersAndBuild(userIds, build);

        List<InvitationPrincipal> invitationPrincipals = createInvitationPrincipals(invitation, users);
        invitationPrincipalRepository.saveAll(invitationPrincipals);
        return invitationMapper.toDto(invitation);
    }

    @Override
    @Transactional
    public InvitationResponseDto save(InvitationCreateRequestDto requestDto) {
        if (invitationRepository.existsByBuildId(requestDto.getBuildId())) {
            throw new EntityConflictException("Invitation already exists");
        }

        Build build = buildService.getBuildOrThrow(requestDto.getBuildId());
        List<User> users = validateUsersAndBuild(requestDto.getUserIds(), build);

        Invitation invitation = invitationMapper.toEntity(requestDto);
        invitation.setBuild(build);
        Invitation savedInvitation = invitationRepository.save(invitation);

        List<InvitationPrincipal> invitationPrincipals = createInvitationPrincipals(savedInvitation, users);
        invitationPrincipalRepository.saveAll(invitationPrincipals);
        savedInvitation.setInvitedUsers(invitationPrincipals);
        return invitationMapper.toDto(savedInvitation);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Invitation invitation = getInvitationOrThrow(id);
        if (!Objects.equals(invitation.getBuild().getUser().getId(), userInfo.getUserId())
                || userInfo.getRole().contains(Role.ROLE_ADMIN.getKeycloakRoleName())) {
            throw new NoAccessException(String.format("User with uuid: %s can`t do this", userInfo.getUserId()));
        }
        invitationRepository.delete(invitation);
    }

    @Override
    @Transactional
    public void deleteUser(Long id, String userId) {
        Invitation invitation = getInvitationOrThrow(id);
        InvitationPrincipal invitationPrincipal = getInvitationPrincipalOrThrow(userId, invitation);
        if (!Objects.equals(invitation.getBuild().getUser().getId(), userInfo.getUserId())
                || userInfo.getRole().contains(Role.ROLE_ADMIN.getKeycloakRoleName())) {
            throw new NoAccessException(String.format("User with uuid: %s can`t do this", userInfo.getUserId()));
        }
        invitationPrincipalRepository.delete(invitationPrincipal);
    }

    private InvitationPrincipal getInvitationPrincipalOrThrow(String userId, Invitation invitation) {
        return invitationPrincipalRepository.findByInvitedUserIdAndInvitationId(userId, invitation.getId()).orElseThrow(
                () -> new EntityNotFountException("Invitation principal not found")
        );
    }

    private Invitation getInvitationOrThrow(Long id) {
        return invitationRepository.findById(id).orElseThrow(() ->
                new EntityNotFountException("Invitation", id)
        );
    }

    private List<User> validateUsersAndBuild(List<String> userIds, Build build) {
        List<User> users = userService.findUsersByIds(userIds);
        if (users.isEmpty()) {
            throw new EntityNotFountException("No users found");
        }
        if (users.stream().anyMatch(user -> user.getId().equals(userInfo.getUserId()))) {
            throw new EntityConflictException("User can't invite himself");
        }
        if (Boolean.FALSE.equals(build.getIsPrivate())) {
            throw new EntityConflictException("Build is not private");
        }
        if (!Objects.equals(build.getUser().getId(), userInfo.getUserId())) {
            throw new NoAccessException("Only user who created build can invite");
        }
        if (invitationPrincipalRepository.existsByUsersAndBuild(users, build)) {
            throw new EntityConflictException("One or more users already invited to the build");
        }
        return users;
    }

    private List<InvitationPrincipal> createInvitationPrincipals(Invitation invitation, List<User> users) {
        List<InvitationPrincipal> invitationPrincipals = new ArrayList<>();
        for (User user : users) {
            InvitationPrincipal invitationPrincipal = new InvitationPrincipal();
            invitationPrincipal.setInvitation(invitation);
            invitationPrincipal.setInvitedUser(user);
            invitationPrincipals.add(invitationPrincipal);
        }
        return invitationPrincipals;
    }
}
