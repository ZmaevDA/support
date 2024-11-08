package ru.zmaev.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.commonlib.exception.EntityConflictException;
import ru.zmaev.commonlib.exception.EntityNotFountException;
import ru.zmaev.commonlib.exception.NoAccessException;
import ru.zmaev.commonlib.model.dto.UserInfo;
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
import ru.zmaev.service.UserService;
import ru.zmaev.service.impl.InvitationServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvitationServiceTests {

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private InvitationPrincipalRepository invitationPrincipalRepository;

    @Mock
    private InvitationMapper invitationMapper;

    @Mock
    private BuildService buildService;

    @Mock
    private UserService userService;

    @Mock
    private UserInfo userInfo;

    @InjectMocks
    private InvitationServiceImpl invitationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Invitation invitation = new Invitation();
        InvitationResponseDto dto = new InvitationResponseDto();
        Page<Invitation> invitationPage = new PageImpl<>(Collections.singletonList(invitation), pageable, 1);

        when(invitationRepository.findAll(pageable)).thenReturn(invitationPage);
        when(invitationMapper.toDto(invitation)).thenReturn(dto);

        Page<InvitationResponseDto> result = invitationService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    void findById() {
        Long id = 1L;
        Invitation invitation = new Invitation();
        InvitationResponseDto dto = new InvitationResponseDto();

        when(invitationRepository.findById(id)).thenReturn(Optional.of(invitation));
        when(invitationMapper.toDto(invitation)).thenReturn(dto);

        InvitationResponseDto result = invitationService.findById(id);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void findByIdThrowsEntityNotFountException() {
        Long id = 1L;

        when(invitationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFountException.class, () -> invitationService.findById(id));
    }

    @Test
    @Transactional
    void save() {
        InvitationCreateRequestDto requestDto = new InvitationCreateRequestDto();
        requestDto.setUserIds(List.of("uuid2", "uuid3"));
        requestDto.setBuildId(1L);

        User invitedUser = new User();
        invitedUser.setId("uuid2");

        User buildUser = new User();
        buildUser.setId("uuid1");
        Build build = new Build();
        build.setUser(buildUser);
        Invitation invitation = new Invitation();
        Invitation savedInvitation = new Invitation();
        InvitationResponseDto dto = new InvitationResponseDto();

        when(userService.findUsersByIds(requestDto.getUserIds())).thenReturn(List.of(invitedUser));
        when(userInfo.getUserId()).thenReturn("uuid1");
        when(buildService.getBuildOrThrow(requestDto.getBuildId())).thenReturn(build);
        when(invitationPrincipalRepository.existsByUsersAndBuild(List.of(invitedUser), build)).thenReturn(false);
        when(invitationMapper.toEntity(requestDto)).thenReturn(invitation);
        when(invitationRepository.save(invitation)).thenReturn(savedInvitation);
        when(invitationMapper.toDto(savedInvitation)).thenReturn(dto);

        InvitationResponseDto result = invitationService.save(requestDto);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void addUsersToInvitation() {
        InvitationCreateRequestDto requestDto = new InvitationCreateRequestDto();
        requestDto.setUserIds(List.of("uuid2", "uuid3"));
        requestDto.setBuildId(1L);

        User invitedUser = new User();
        invitedUser.setId("uuid2");

        User buildUser = new User();
        buildUser.setId("uuid1");
        Build build = new Build();
        build.setUser(buildUser);
        Invitation invitation = new Invitation();
        invitation.setBuild(build);
        InvitationResponseDto dto = new InvitationResponseDto();
        dto.setId(1L);

        when(invitationRepository.findById(anyLong())).thenReturn(Optional.of(invitation));
        when(userService.findUsersByIds(requestDto.getUserIds())).thenReturn(List.of(invitedUser));
        when(userInfo.getUserId()).thenReturn("uuid1");
        when(buildService.getBuildOrThrow(requestDto.getBuildId())).thenReturn(build);
        when(invitationPrincipalRepository.existsByUsersAndBuild(List.of(invitedUser), build)).thenReturn(false);
        when(invitationMapper.toEntity(requestDto)).thenReturn(invitation);
        when(invitationMapper.toDto(invitation)).thenReturn(dto);

        InvitationResponseDto result = invitationService.addUsersToInvitation(1L, Arrays.asList("uuid2", "uuid3"));

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void saveThrowsEntityNotFountException() {
        InvitationCreateRequestDto requestDto = new InvitationCreateRequestDto();
        requestDto.setUserIds(List.of("uuid1", "uuid2", "uuid3"));
        requestDto.setBuildId(1L);

        when(userService.findUsersByIds(requestDto.getUserIds())).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFountException.class, () -> invitationService.save(requestDto));
    }

    @Test
    void saveThrowsEntityConflictException() {
        InvitationCreateRequestDto requestDto = new InvitationCreateRequestDto();
        requestDto.setUserIds(List.of("uuid1", "uuid2", "uuid3"));
        requestDto.setBuildId(1L);

        User buildUser = new User();
        buildUser.setId("uuid1");
        Build build = new Build();
        build.setUser(buildUser);

        when(userService.findUsersByIds(requestDto.getUserIds())).thenReturn(List.of(buildUser));
        when(userInfo.getUserId()).thenReturn("uuid1");

        assertThrows(EntityConflictException.class, () -> invitationService.save(requestDto));
    }

    @Test
    void saveThrowsNoAccessException() {
        InvitationCreateRequestDto requestDto = new InvitationCreateRequestDto();
        requestDto.setUserIds(List.of("uuid1", "uuid2", "uuid3"));
        requestDto.setBuildId(1L);

        User user = new User();
        user.setId("uuid1");
        Build build = new Build();
        build.setUser(new User()); // не тот пользователь
        build.getUser().setId("uuid1");

        when(userService.findUsersByIds(requestDto.getUserIds())).thenReturn(List.of(user));
        when(userInfo.getUserId()).thenReturn("uuid2");
        when(buildService.getBuildOrThrow(requestDto.getBuildId())).thenReturn(build);

        assertThrows(NoAccessException.class, () -> invitationService.save(requestDto));
    }

    @Test
    void delete() {
        Long id = 1L;
        Invitation invitation = new Invitation();

        User buildUser = new User();
        buildUser.setId("uuid1");
        Build build = new Build();
        build.setUser(buildUser);
        invitation.setBuild(build);

        when(userInfo.getUserId()).thenReturn("uuid1");
        when(invitationRepository.findById(id)).thenReturn(Optional.of(invitation));

        invitationService.delete(id);

        verify(invitationRepository, times(1)).delete(invitation);
    }

    @Test
    void deleteThrowsNoAccessException() {
        Long id = 1L;
        Invitation invitation = new Invitation();

        User buildUser = new User();
        buildUser.setId("uuid1");
        Build build = new Build();
        build.setUser(buildUser);
        invitation.setBuild(build);

        when(invitationRepository.findById(id)).thenReturn(Optional.of(invitation));

        assertThrows(NoAccessException.class, () -> invitationService.delete(id));
    }

    @Test
    void deleteThrowsEntityNotFountException() {
        Long id = 1L;

        when(invitationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFountException.class, () -> invitationService.delete(id));
    }

    @Test
    void deleteUser() {
        User invitedUser = new User();
        invitedUser.setId("uuid2");

        User buildUser = new User();
        buildUser.setId("uuid1");
        Build build = new Build();
        build.setUser(buildUser);
        Invitation invitation = new Invitation();
        invitation.setBuild(build);
        invitation.setId(1L);
        InvitationPrincipal invitationPrincipal = new InvitationPrincipal();
        invitationPrincipal.setId(1L);
        invitationPrincipal.setInvitedUser(invitedUser);
        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitation));
        when(invitationPrincipalRepository.findByInvitedUserIdAndInvitationId(anyString(), anyLong())).thenReturn(Optional.of(invitationPrincipal));
        when(userInfo.getUserId()).thenReturn("uuid1");
        invitationService.deleteUser(1L, "uuid2");

        verify(invitationRepository, times(1)).findById(1L);
        verify(invitationPrincipalRepository, times(1)).findByInvitedUserIdAndInvitationId("uuid2", 1L);
        verify(invitationPrincipalRepository, times(1)).delete(invitationPrincipal);
    }

    @Test
    void deleteUserThrowsNoAccessException() {
        User invitedUser = new User();
        invitedUser.setId("uuid2");

        User buildUser = new User();
        buildUser.setId("uuid2");
        Build build = new Build();
        build.setUser(buildUser);
        Invitation invitation = new Invitation();
        invitation.setBuild(build);
        invitation.setId(1L);
        InvitationPrincipal invitationPrincipal = new InvitationPrincipal();
        invitationPrincipal.setId(1L);
        invitationPrincipal.setInvitedUser(invitedUser);
        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitation));
        when(invitationPrincipalRepository.findByInvitedUserIdAndInvitationId(anyString(), anyLong())).thenReturn(Optional.of(invitationPrincipal));

        assertThrows(NoAccessException.class, () -> invitationService.deleteUser(1L, "uuid2"));
    }

}

