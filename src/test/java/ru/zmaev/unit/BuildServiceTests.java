package ru.zmaev.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import ru.zmaev.commonlib.exception.EntityNotFountException;
import ru.zmaev.commonlib.exception.NoAccessException;
import ru.zmaev.commonlib.model.dto.UserInfo;
import ru.zmaev.domain.document.BuildDocument;
import ru.zmaev.domain.dto.request.AshesOfWarWeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildUpdateRequestDto;
import ru.zmaev.domain.dto.response.AshesOfWarWeaponResponseDto;
import ru.zmaev.domain.dto.response.BuildResponseDto;
import ru.zmaev.domain.dto.response.CommentResponseDto;
import ru.zmaev.domain.entity.Character;
import ru.zmaev.domain.entity.*;
import ru.zmaev.domain.enums.ReactionType;
import ru.zmaev.domain.mapper.*;
import ru.zmaev.repository.*;
import ru.zmaev.repository.elasticsearch.BuildElasticsearchRepository;
import ru.zmaev.service.*;
import ru.zmaev.service.impl.BuildServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildServiceTests {

    @Mock
    private BuildRepository buildRepository;
    @Mock
    private AshesOfWarWeaponRepository ashesOfWarWeaponRepository;
    @Mock
    private BuildWeaponRepository buildWeaponRepository;
    @Mock
    private ResistRepository resistRepository;
    @Mock
    private BuildInventoryItemRepository buildInventoryItemRepository;
    @Mock
    private BuildElasticsearchRepository buildElasticsearchRepository;
    @Mock
    private ViewRepository viewRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private BuildMapper buildMapper;
    @Mock
    private InventoryItemMapper inventoryItemMapper;
    @Mock
    private CharacterMapper characterMapper;
    @Mock
    private ResistMapper resistMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private WeaponMapper weaponMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private RabbitMQProducerService rabbitMQProducerService;
    @Mock
    private UserService userService;
    @Mock
    private WeaponService weaponService;
    @Mock
    private InventoryItemService inventoryItemService;
    @Mock
    private AshesOfWarService ashesOfWarService;

    @Mock
    private UserInfo userInfo;

    private BuildServiceImpl buildService;

    private Reaction reaction;
    private Comment comment;
    private User user;
    private Build build;
    private BuildDocument buildDocument;
    private BuildResponseDto buildResponseDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Set<Character> characters = new HashSet<>(Arrays.asList(new Character(), new Character()));

        Set<BuildWeapon> buildWeapons = new HashSet<>(Arrays.asList(new BuildWeapon(), new BuildWeapon()));

        List<AshesOfWarWeaponResponseDto> ashesOfWarWeaponResponseDtos =
                Arrays.asList(new AshesOfWarWeaponResponseDto(), new AshesOfWarWeaponResponseDto());
        user = new User();
        user.setId("uuid1");
        user.setUsername("User");

        build = new Build(1L,
                "Build",
                "Description",
                false,
                0D,
                null,
                characters,
                buildWeapons,
                null,
                null,
                null,
                null,
                user);
        buildDocument = new BuildDocument(1L, "Build", "Description");

        buildResponseDto = new BuildResponseDto(
                1L,
                "1L",
                "Build",
                "Description",
                false,
                ashesOfWarWeaponResponseDtos,
                null);

        reaction = new Reaction();

        comment = new Comment();

        buildService = new BuildServiceImpl(
                buildRepository,
                ashesOfWarWeaponRepository,
                buildWeaponRepository,
                buildInventoryItemRepository,
                resistRepository,
                viewRepository,
                commentRepository,
                reactionRepository,
                buildElasticsearchRepository,
                buildMapper,
                weaponMapper,
                inventoryItemMapper,
                characterMapper,
                resistMapper,
                userMapper,
                commentMapper,
                rabbitMQProducerService,
                userService,
                inventoryItemService,
                weaponService,
                ashesOfWarService,
                userInfo);
    }

    @Test
    void findAllPagedPublic() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, Build_.RATING));
        Build build = new Build();
        Page<Build> buildsPage = new PageImpl<>(List.of(build));

        when(buildRepository.findByIsPrivateFalse(pageable)).thenReturn(buildsPage);
        when(buildMapper.buildToBuildResponseDto(build)).thenReturn(new BuildResponseDto());

        Page<BuildResponseDto> result = buildService.findAllPaged(pageable, false);

        assertEquals(1, result.getContent().size());
        verify(buildRepository, never()).findAll(pageable);
        verify(buildRepository, times(1)).findByIsPrivateFalse(pageable);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, Build_.RATING));
        Build build = new Build();
        Page<Build> buildsPage = new PageImpl<>(List.of(build));

        when(buildRepository.findAll(pageable)).thenReturn(buildsPage);
        when(buildMapper.buildToBuildResponseDto(build)).thenReturn(new BuildResponseDto());

        Page<BuildResponseDto> result = buildService.findAllPaged(pageable, true);

        assertEquals(1, result.getContent().size());
        verify(buildRepository, times(1)).findAll(pageable);
        verify(buildRepository, never()).findByIsPrivateFalse(pageable);
    }

    @Test
    void addReaction() {
        Reaction reaction = new Reaction();
        reaction.setId(1L);
        when(userService.findUserByIdOrThrow(anyString())).thenReturn(user);
        when(userInfo.getUserId()).thenReturn("uuid1");
        when(buildRepository.findById(anyLong())).thenReturn(Optional.of(build));

        when(reactionRepository.findByBuildAndUser(any(Build.class), any(User.class))).thenReturn(Optional.of(reaction));
        when(reactionRepository.save(any(Reaction.class))).thenReturn(reaction);

        buildService.addReaction(1L, "uuid1", ReactionType.LIKE);

        verify(reactionRepository, times(1)).save(reaction);
    }

    @Test
    void addReactionThrowsNoAccessException() {
        reaction.setId(1L);
        when(userService.findUserByIdOrThrow(anyString())).thenReturn(user);
        when(userInfo.getUserId()).thenReturn("uuid2");

        assertThrows(NoAccessException.class, () -> buildService.addReaction(1L, "uuid1", ReactionType.LIKE));
    }

    @Test
    void deleteReaction() {
        Reaction reaction = new Reaction();
        reaction.setId(1L);
        reaction.setBuild(build);
        when(userInfo.getUserId()).thenReturn("uuid1");
        when(reactionRepository.findById(anyLong())).thenReturn(Optional.of(reaction));

        buildService.deleteReaction(1L);

        verify(reactionRepository, times(1)).delete(reaction);
    }

    @Test
    void deleteReactionThrowsNoAccessException() {
        reaction.setId(1L);
        reaction.setBuild(build);
        when(reactionRepository.findById(anyLong())).thenReturn(Optional.of(reaction));
        when(userInfo.getUserId()).thenReturn("uuid2");

        assertThrows(NoAccessException.class, () -> buildService.deleteReaction(1L));
    }

    @Test
    void addComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("content");
        comment.setBuild(build);
        when(userService.findUserByIdOrThrow(anyString())).thenReturn(user);
        when(userInfo.getUserId()).thenReturn("uuid1");
        when(buildRepository.findById(anyLong())).thenReturn(Optional.of(build));
        when(commentRepository.findByBuildAndUser(any(Build.class), any(User.class))).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        buildService.addComment(1L, "uuid1", "content");

        verify(commentRepository, times(1)).save(comment);
        assertEquals("content", comment.getContent());
    }

    @Test
    void addCommentThrowNoAccessException() {
        when(userService.findUserByIdOrThrow(anyString())).thenReturn(user);
        when(userInfo.getUserId()).thenReturn("uuid2");

        assertThrows(NoAccessException.class, () -> buildService.addComment(1L, "uuid1", "content"));
    }

    @Test
    void findAllCommentsByBuildSortedByUpdatedAt() {
        Comment comment1 = new Comment();
        comment1.setId(1L);
        Comment comment2 = new Comment();
        comment2.setId(2L);
        CommentResponseDto commentResponseDto1 = new CommentResponseDto();
        commentResponseDto1.setId(1L);
        CommentResponseDto commentResponseDto2 = new CommentResponseDto();
        commentResponseDto1.setId(2L);
        Long buildId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> comments = Arrays.asList(comment1, comment2);
        Page<Comment> commentPage = new PageImpl<>(comments);

        when(buildRepository.findById(anyLong())).thenReturn(Optional.of(build));
        when(commentRepository.findAllByBuild(build, pageable)).thenReturn(commentPage);
        when(commentMapper.toDto(comment1)).thenReturn(commentResponseDto1);
        when(commentMapper.toDto(comment2)).thenReturn(commentResponseDto2);

        Page<CommentResponseDto> result = buildService.findAllCommentsByBuildSortedByUpdatedAt(buildId, pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(commentResponseDto1, result.getContent().get(0));
        assertEquals(commentResponseDto2, result.getContent().get(1));
    }

    @Test
    void deleteComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(user);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userInfo.getUserId()).thenReturn("uuid1");

        buildService.deleteComment(1L);

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteCommentThrowsEntityNotFountException() {
        comment.setId(1L);
        comment.setUser(user);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFountException.class, () -> buildService.deleteComment(2L));
    }

    @Test
    void deleteCommentThrowsNoAccessException() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        assertThrows(NoAccessException.class, () -> buildService.deleteComment(2L));
    }

    @Test
    void recalculateRating() {
        int likeAmount = 10;
        int dislikeAmount = 5;
        int viewsAmount = 100;
        int commentsAmount = 20;

        when(reactionRepository.countByBuildAndReactionType(build, ReactionType.LIKE)).thenReturn(likeAmount);
        when(reactionRepository.countByBuildAndReactionType(build, ReactionType.DISLIKE)).thenReturn(dislikeAmount);
        when(viewRepository.countByBuild(build)).thenReturn(viewsAmount);
        when(commentRepository.countByBuild(build)).thenReturn(commentsAmount);

        buildService.recalculateRating(build);

        double reactionsScore = (likeAmount - dislikeAmount) * 0.7f;
        double viewsScore = viewsAmount * 0.1f;
        double commentsScore = commentsAmount * 0.2f;
        double expectedRating = reactionsScore + viewsScore + commentsScore;

        assertEquals(expectedRating, build.getRating());
        verify(buildRepository, times(1)).save(build);
    }

    @Test
    void findAllUsersAllowed() {
        Pageable pageable = PageRequest.of(0, 10);
        String userId = "user1";
        Build build = new Build();
        Page<Build> buildsPage = new PageImpl<>(List.of(build));

        when(buildRepository.findBuildsByInvitedUserId(userId, pageable)).thenReturn(buildsPage);
        when(buildMapper.buildToBuildResponseDto(build)).thenReturn(new BuildResponseDto());

        Page<BuildResponseDto> result = buildService.findAllUsersAllowed(pageable, userId);

        assertEquals(1, result.getContent().size());
        verify(userService, times(1)).findUserByIdOrThrow(userId);
        verify(buildRepository, times(1)).findBuildsByInvitedUserId(userId, pageable);
        verify(buildMapper, times(1)).buildToBuildResponseDto(build);
    }

    @Test
    @WithMockUser("admin")
    void findById() {
        Long buildId = 1L;
        when(buildRepository.findById(anyLong())).thenReturn(Optional.of(build));
        when(buildMapper.buildToBuildResponseDto(build)).thenReturn(buildResponseDto);

        BuildResponseDto result = buildService.findById(buildId, null);

        assertEquals(buildResponseDto, result);
        verify(buildMapper, times(1)).buildToBuildResponseDto(build);
    }

    @Test
    void create() {
        AshesOfWarWeaponCreateRequestDto ar1 = new AshesOfWarWeaponCreateRequestDto(1L, 1L);
        List<AshesOfWarWeaponCreateRequestDto> ashesOfWarWeaponCreateRequestDtos = List.of(ar1);

        BuildCreateRequestDto buildCreateRequestDto = new BuildCreateRequestDto();
        buildCreateRequestDto.setAshesOfWarWeapons(ashesOfWarWeaponCreateRequestDtos);
        buildCreateRequestDto.setBuildName("Build");
        buildCreateRequestDto.setBuildDescription("Build Description");
        Weapon weapon = new Weapon();
        weapon.setId(1L);
        AshesOfWar ashesOfWar = new AshesOfWar();
        ashesOfWar.setId(1L);
        BuildWeapon b = new BuildWeapon();
        b.setId(1L);
        List<BuildWeapon> buildWeapons = List.of(b);

        when(weaponService.getWeaponOrThrow(any(Long.class))).thenReturn(weapon);
        when(ashesOfWarService.getAshOfWarOrThrow(any(Long.class))).thenReturn(ashesOfWar);
        when(ashesOfWarWeaponRepository.save(any(AshesOfWarWeapon.class))).thenAnswer(invocation -> {
            AshesOfWarWeapon ashesOfWarWeapon = invocation.getArgument(0);
            ashesOfWarWeapon.setId(1L);
            return ashesOfWarWeapon;
        });
        when(userInfo.getUserId()).thenReturn("1L");
        when(buildMapper.toEntity(buildCreateRequestDto)).thenReturn(build);
        when(buildRepository.save(any(Build.class))).thenAnswer(invocation -> {
            Build savedBuild = invocation.getArgument(0);
            savedBuild.setId(1L);
            return savedBuild;
        });

        when(buildMapper.buildToBuildResponseDto(build)).thenReturn(buildResponseDto);
        when(buildWeaponRepository.saveAll(any())).thenReturn(buildWeapons);
        when(buildMapper.toDocument(build)).thenReturn(buildDocument);
        when(buildElasticsearchRepository.save(any())).thenReturn(buildDocument);
        BuildResponseDto result = buildService.create(buildCreateRequestDto);

        Assertions.assertThat(result).isNotNull().isEqualTo(buildResponseDto);
    }

    @Test
    void update() {
        AshesOfWarWeaponCreateRequestDto ar1 = new AshesOfWarWeaponCreateRequestDto(1L, 1L);

        List<AshesOfWarWeaponCreateRequestDto> aowulist = List.of(ar1);
        BuildUpdateRequestDto buildUpdateRequestDto = new BuildUpdateRequestDto();
        buildUpdateRequestDto.setId(1L);
        buildUpdateRequestDto.setBuildName("Build");
        buildUpdateRequestDto.setBuildDescription("Build Description");
        buildUpdateRequestDto.setAshesOfWars(aowulist);

        Weapon weapon = new Weapon();
        weapon.setId(1L);
        AshesOfWar ashesOfWar = new AshesOfWar();
        ashesOfWar.setId(1L);
        BuildWeapon b = new BuildWeapon();
        b.setId(1L);
        List<BuildWeapon> buildWeapons = List.of(b);

        Build build = new Build();
        build.setId(1L);
        build.setName("Build");
        build.setDescription("Build Description");
        build.setUser(new User());

        Build updatedBuild = new Build();
        updatedBuild.setId(1L);
        updatedBuild.setName("Updated Build");
        updatedBuild.setDescription("Updated Build Description");
        updatedBuild.setUser(new User());

        when(weaponService.getWeaponOrThrow(any(Long.class))).thenReturn(weapon);
        when(ashesOfWarService.getAshOfWarOrThrow(any(Long.class))).thenReturn(ashesOfWar);
        when(ashesOfWarWeaponRepository.save(any(AshesOfWarWeapon.class))).thenAnswer(invocation -> {
            AshesOfWarWeapon ashesOfWarWeapon = invocation.getArgument(0);
            ashesOfWarWeapon.setId(1L);
            return ashesOfWarWeapon;
        });
        when(userInfo.getUserId()).thenReturn(build.getUser().getId());
        when(buildRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(build));
        when(buildRepository.save(any(Build.class))).thenAnswer(invocation -> {
            Build savedBuild = invocation.getArgument(0);
            savedBuild.setId(1L);
            return savedBuild;
        });
        when(buildMapper.buildToBuildResponseDto(any(Build.class))).thenReturn(buildResponseDto);
        when(buildWeaponRepository.saveAll(any())).thenReturn(buildWeapons);
        when(buildMapper.toDocument(build)).thenReturn(buildDocument);
        when(buildElasticsearchRepository.save(any())).thenReturn(buildDocument);

        BuildResponseDto result = buildService.update(buildUpdateRequestDto);

        Assertions.assertThat(result).isNotNull().isEqualTo(buildResponseDto);
    }

    @Test
    void delete() {
        Long buildId = 1L;
        build.setId(buildId);
        build.setUser(user);

        when(userInfo.getUserId()).thenReturn("uuid1");
        when(buildRepository.findById(buildId)).thenReturn(Optional.of(build));

        buildService.delete(buildId);

        verify(buildRepository, times(1)).deleteById(buildId);
    }


    @Test
    void findAllPublicSearch() {
        Pageable pageable = mock(Pageable.class);
        String search = "Build";
        List<BuildDocument> buildDocumentList = List.of(
                new BuildDocument(1L, "Build1", "Description1"),
                new BuildDocument(2L, "Build2", "Description2")
        );
        Build build1 = new Build();
        build1.setId(1L);
        build1.setName("Build1");
        build1.setDescription("Description1");
        Build build2 = new Build();
        build2.setId(2L);
        build2.setName("Build2");
        build2.setDescription("Description2");
        BuildResponseDto buildResponseDto1 = new BuildResponseDto();
        buildResponseDto1.setId(1L);
        buildResponseDto1.setBuildName("Build1");
        buildResponseDto1.setBuildDescription("Description1");
        BuildResponseDto buildResponseDto2 = new BuildResponseDto();
        buildResponseDto1.setId(2L);
        buildResponseDto1.setBuildName("Build2");
        buildResponseDto1.setBuildDescription("Description2");
        Page<BuildDocument> buildDocuments = new PageImpl<>(buildDocumentList, pageable, buildDocumentList.size());

        List<Build> builds = List.of(
                build1,
                build2
        );

        when(buildElasticsearchRepository.findAllSearch(eq(search), any(Pageable.class))).thenReturn(buildDocuments);
        when(buildRepository.findAllById(buildDocumentList.stream().map(BuildDocument::getId).toList())).thenReturn(builds);
        when(buildMapper.buildToBuildResponseDto(builds.get(0))).thenReturn(buildResponseDto1);
        when(buildMapper.buildToBuildResponseDto(builds.get(1))).thenReturn(buildResponseDto2);

        Page<BuildResponseDto> result = buildService.findAllSearch(search, pageable);

        verify(buildElasticsearchRepository).findAllSearch(eq(search), any(Pageable.class));
        verify(buildRepository).findAllById(buildDocumentList.stream().map(BuildDocument::getId).toList());

        List<BuildResponseDto> expectedContent = List.of(buildResponseDto1, buildResponseDto2);
        Assertions.assertThat(result.getContent()).isNotNull().isEqualTo(expectedContent);
    }

}
