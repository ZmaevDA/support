package ru.zmaev.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zmaev.commonlib.exception.EntityBadRequestException;
import ru.zmaev.commonlib.exception.EntityConflictException;
import ru.zmaev.commonlib.exception.EntityNotFountException;
import ru.zmaev.commonlib.exception.NoAccessException;
import ru.zmaev.commonlib.model.dto.BuildMessage;
import ru.zmaev.commonlib.model.dto.UserInfo;
import ru.zmaev.commonlib.model.dto.response.EntityIsExistsResponseDto;
import ru.zmaev.commonlib.model.enums.Role;
import ru.zmaev.domain.document.BuildDocument;
import ru.zmaev.domain.dto.request.AshesOfWarWeaponCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildCreateRequestDto;
import ru.zmaev.domain.dto.request.BuildUpdateRequestDto;
import ru.zmaev.domain.dto.response.BuildDetailedResponseDto;
import ru.zmaev.domain.dto.response.BuildResponseDto;
import ru.zmaev.domain.dto.response.CharacterDetailedResponseDto;
import ru.zmaev.domain.dto.response.CommentResponseDto;
import ru.zmaev.domain.entity.Character;
import ru.zmaev.domain.entity.*;
import ru.zmaev.domain.enums.ItemType;
import ru.zmaev.domain.enums.ReactionType;
import ru.zmaev.domain.mapper.*;
import ru.zmaev.repository.*;
import ru.zmaev.repository.elasticsearch.BuildElasticsearchRepository;
import ru.zmaev.service.*;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuildServiceImpl implements BuildService {

    private final BuildRepository buildRepository;
    private final AshesOfWarWeaponRepository ashesOfWarWeaponRepository;
    private final BuildWeaponRepository buildWeaponRepository;
    private final BuildInventoryItemRepository buildInventoryItemRepository;
    private final ResistRepository resistRepository;
    private final ViewRepository viewRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final BuildElasticsearchRepository buildElasticsearchRepository;

    private final BuildMapper buildMapper;
    private final WeaponMapper weaponMapper;
    private final InventoryItemMapper inventoryItemMapper;
    private final CharacterMapper characterMapper;
    private final ResistMapper resistMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    private final RabbitMQProducerService rabbitMQProducerService;
    private final UserService userService;
    private final InventoryItemService inventoryItemService;
    private final WeaponService weaponService;
    private final AshesOfWarService ashesOfWarService;

    private final UserInfo userInfo;

    private static final float REACTION_WEIGHT = 0.7f;
    private static final float VIEWS_WEIGHT = 0.1f;
    private static final float COMMENTS_WEIGHT = 0.2f;

    @Override
    public Page<BuildResponseDto> findAllPaged(Pageable pageable, boolean addPrivate) {
        Page<Build> builds;
        if (addPrivate) {
            log.info("Fetching all builds with pagination: {}", pageable);
            builds = buildRepository.findAll(pageable);
        } else {
            log.info("Fetching all public builds with pagination: {}", pageable);
            builds = buildRepository.findByIsPrivateFalse(pageable);
        }
        return builds.map(buildMapper::buildToBuildResponseDto);
    }

    @Override
    public List<Build> findAll() {
        return buildRepository.findAll();
    }

    @Override
    public Page<BuildResponseDto> findAllSearch(String search, Pageable pageable) {
        log.info("Fetching all builds with pagination form elasticsearch: {}", pageable);
        Page<BuildDocument> buildDocuments = buildElasticsearchRepository.findAllSearch(search, pageable);
        log.info("Fetching all builds with pagination from jpa: {}", pageable);
        List<Build> builds = buildRepository.findAllById(buildDocuments.map(BuildDocument::getId).stream().toList());
        return new PageImpl<>(builds.stream().map(buildMapper::buildToBuildResponseDto).toList(), pageable, builds.size());
    }

    @Override
    public Page<BuildResponseDto> findAllUsersAllowed(Pageable pageable, String userId) {
        userService.findUserByIdOrThrow(userId);
        Page<Build> builds = buildRepository.findBuildsByInvitedUserId(userId, pageable);
        return builds.map(buildMapper::buildToBuildResponseDto);
    }

    @Override
    @Transactional
    public BuildResponseDto findById(Long id, String token) {
        log.info("Fetching build by id: {}", id);

        Build build = buildRepository.findById(id).orElseThrow(() ->
                new EntityNotFountException("Build not found with id: " + id));
        addView(build);
        log.info("Fetched build by id: {}", id);

        if (Boolean.TRUE.equals(build.getIsPrivate())) {
            checkAccessToPrivateBuild(build, token);
        }

        return buildMapper.buildToBuildResponseDto(build);
    }

    @Override
    @Transactional
    public BuildDetailedResponseDto findByIdDetailed(Long id, String token) {
        log.info("Fetching detailed build by id: {}", id);
        Build build = getBuildOrThrow(id);
        log.info("Fetched detailed build by id: {}", id);
        addView(build);
        if (Boolean.TRUE.equals(build.getIsPrivate())) {
            checkAccessToPrivateBuild(build, token);
        }

        Set<BuildWeapon> buildWeapons = new HashSet<>(build.getBuildWeapons());
        Set<BuildInventoryItem> buildInventoryItems = new HashSet<>(build.getBuildInventoryItems());

        double totalWeight = buildWeapons.stream()
                .map(BuildWeapon::getAshesOfWarWeapon)
                .map(AshesOfWarWeapon::getWeapon)
                .mapToDouble(Weapon::getWeight)
                .sum();

        totalWeight += buildInventoryItems.stream()
                .map(BuildInventoryItem::getInventoryItem)
                .mapToDouble(InventoryItem::getWeight)
                .sum();

        return new BuildDetailedResponseDto(
                build.getId(),
                build.getUser().getId(),
                build.getName(),
                build.getDescription(),
                totalWeight,
                resistMapper.toDto(resistRepository.findSumOfResistsByBuildId(build.getId())),
                getDetailedCharacters(build),
                buildInventoryItems.stream()
                        .map(BuildInventoryItem::getInventoryItem)
                        .map(inventoryItemMapper::toDto).toList(),
                buildWeapons.stream()
                        .map(b -> b.getAshesOfWarWeapon().getWeapon())
                        .map(weaponMapper::toDto).toList()
        );
    }

    @Override
    @Transactional
    public void addReaction(Long buildId, String userId, ReactionType reactionType) {
        User user = userService.findUserByIdOrThrow(userId);

        if (!Objects.equals(user.getId(), userInfo.getUserId())) {
            throw new NoAccessException("You do not have access to reaction");
        }

        Build build = getBuildOrThrow(buildId);
        Reaction reaction = reactionRepository.findByBuildAndUser(build, user).orElse(new Reaction());

        if (reaction.getReactionType() == null) {
            reaction.setBuild(build);
            reaction.setUser(user);
            reaction.setReactedAt(Instant.now());
        }

        reaction.setUpdatedAt(Instant.now());
        reaction.setReactionType(reactionType);
        reactionRepository.save(reaction);
    }

    @Override
    public void deleteReaction(Long reactionId) {
        log.info("Deleting reaction with id: " + reactionId);

        Reaction reaction = reactionRepository.findById(reactionId).orElseThrow(() ->
                new EntityNotFountException("Reaction with id: " + reactionId + "not found!")
        );

        if (!Objects.equals(reaction.getBuild().getUser().getId(), userInfo.getUserId())) {
            throw new NoAccessException("You do not have access to reaction");
        }
        reactionRepository.delete(reaction);
        log.info("Reaction deleted with id: " + reaction.getId());
    }

    @Override
    public void addComment(Long buildId, String userId, String content) {
        User user = userService.findUserByIdOrThrow(userId);

        if (!Objects.equals(user.getId(), userInfo.getUserId())) {
            throw new NoAccessException("You do not have access to reaction");
        }

        Build build = getBuildOrThrow(buildId);
        Comment comment = commentRepository.findByBuildAndUser(build, user).orElse(new Comment());

        if (comment.getContent() == null) {
            comment.setBuild(build);
            comment.setUser(user);
            comment.setCommentedAt(Instant.now());
        }

        comment.setUpdatedAt(Instant.now());
        comment.setContent(content);
        commentRepository.save(comment);
    }

    @Override
    public Page<CommentResponseDto> findAllCommentsByBuildSortedByUpdatedAt(Long id, Pageable pageable) {
        Build build = getBuildOrThrow(id);
        Page<Comment> comments = commentRepository.findAllByBuild(build, pageable);
        return comments.map(commentMapper::toDto);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new EntityNotFountException("Comment not found with id: " + commentId));
        if (!Objects.equals(comment.getUser().getId(), userInfo.getUserId()) &&
                !userInfo.getRole().contains(Role.ROLE_ADMIN.name())) {
            throw new NoAccessException("You do not have access to comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public BuildResponseDto create(BuildCreateRequestDto dto) {
        log.info("Creating new build with request: {}", dto);
        User user = userMapper.toEntity(userService.findById(userInfo.getUserId()));
        Build build = buildMapper.toEntity(dto);
        build.setUser(user);
        build = saveBuild(build, dto.getAshesOfWarWeapons());
        saveBuildInventoryItems(dto.getInventoryItemIds(), build);
        BuildMessage buildMessage = new BuildMessage(
                userInfo.getUserId(),
                build.getId(),
                build.getName(),
                build.getDescription());
        BuildDocument buildDocument = buildMapper.toDocument(build);
        log.info("Saving build to elasticsearch");
        buildElasticsearchRepository.save(buildDocument);
        log.info("Build saved to elasticsearch");
        log.info("Sending message to queue: {}", buildMessage);
        rabbitMQProducerService.sendMessage(buildMessage);
        return buildMapper.buildToBuildResponseDto(build);
    }

    @Override
    @Transactional
    public BuildResponseDto update(BuildUpdateRequestDto buildUpdateRequestDto) {
        log.info("Update build with id: {}", buildUpdateRequestDto.getId());
        Build build = getBuildOrThrow(buildUpdateRequestDto.getId());
        if (!Objects.equals(build.getUser().getId(), userInfo.getUserId())) {
            throw new NoAccessException(userInfo.getUsername(), userInfo.getRole().toString());
        }

        build.setName(buildUpdateRequestDto.getBuildName());
        build.setDescription(buildUpdateRequestDto.getBuildDescription());

        Long buildId = build.getId();
        buildWeaponRepository.deleteAllByBuildId(buildId);
        log.info("BuildWeapons deleted successfully");
        buildInventoryItemRepository.deleteAllByBuildId(buildId);
        log.info("BuildInventoryItems deleted successfully");

        List<AshesOfWarWeapon> ashesOfWarWeaponList = createAshesOfWarWeapons(
                buildUpdateRequestDto.getAshesOfWars()
        );

        createBuildWeapons(build, ashesOfWarWeaponList);
        log.info("BuildInventoryItems created successfully");
        build = buildRepository.save(build);
        saveBuildInventoryItems(buildUpdateRequestDto.getInventoryItemIds(), build);
        log.info("Build updated successfully with id: {}", build.getId());
        BuildDocument buildDocument = buildMapper.toDocument(build);
        log.info("Updating BuildDocument with id: {}", build.getId());
        buildElasticsearchRepository.save(buildDocument);
        log.info("BuildDocument updated successfully with id: {}", build.getId());
        return buildMapper.buildToBuildResponseDto(build);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Build build = getBuildOrThrow(id);
        if (!Objects.equals(build.getUser().getId(), userInfo.getUserId()) &&
                !userInfo.getRole().contains(Role.ROLE_ADMIN.name())) {
            throw new NoAccessException(userInfo.getUsername(), userInfo.getRole().toString());
        }
        log.info("Deleting build with id: {} from postgres", id);
        buildRepository.deleteById(id);
        log.info("Build deleted successfully with id: {} from postgres", id);
        BuildDocument buildDocument = buildMapper.toDocument(build);
        log.info("Deleting build with id: {} from elasticsearch", id);
        buildElasticsearchRepository.delete(buildDocument);
        log.info("Build deleted successfully with id: {} from elasticsearch", id);
    }

    @Override
    public Build getBuildOrThrow(Long id) {
        return buildRepository.findById(id).orElseThrow(() ->
                new EntityNotFountException("Build", id));
    }

    @Override
    public EntityIsExistsResponseDto existsById(Long id) {
        return new EntityIsExistsResponseDto(buildRepository.existsById(id));
    }

    @Transactional
    public void recalculateRating(Build build) {
        int likeAmount = reactionRepository.countByBuildAndReactionType(build, ReactionType.LIKE);
        int dislikeAmount = reactionRepository.countByBuildAndReactionType(build, ReactionType.DISLIKE);
        int viewsAmount = viewRepository.countByBuild(build);
        int commentsAmount = commentRepository.countByBuild(build);

        double reactionsScore = 0;
        if (likeAmount > dislikeAmount) {
            reactionsScore = (likeAmount - dislikeAmount) * REACTION_WEIGHT;
        }

        double viewsScore = viewsAmount * VIEWS_WEIGHT;
        double commentsScore = commentsAmount * COMMENTS_WEIGHT;

        build.setRating(reactionsScore + viewsScore + commentsScore);
        buildRepository.save(build);
    }

    private void addView(Build build) {
        log.info("Creating View");
        User currentUser = userService.findUserByIdOrThrow(userInfo.getUserId());
        if (viewRepository.existsByBuildAndUser(build, currentUser)) {
            log.info("User with id: {} already viewed build with id: {}", userInfo.getUserId(), build.getId());
            return;
        }
        View view = new View();
        view.setBuild(build);
        view.setUser(currentUser);
        viewRepository.save(view);
        log.info("View created successfully");
    }

    private void checkAccessToPrivateBuild(Build build, String token) {
        List<InvitationPrincipal> invitedUsers = Collections.emptyList();
        if (build.getInvitation() != null && build.getInvitation().getInvitedUsers() != null) {
            invitedUsers = build.getInvitation().getInvitedUsers();
        }

        boolean isAdmin = userInfo.getRole().contains(Role.ROLE_ADMIN.name());
        if (isAdmin) {
            return;
        }
        boolean isTokenValid = token != null &&
                !token.isEmpty() && Objects.requireNonNull(build.getInvitation()).getToken().equals(token);
        if (isTokenValid) {
            return;
        }
        boolean isUserInvited = invitedUsers.stream()
                .anyMatch(user -> user.getInvitedUser().getId().equals(userInfo.getUserId()));
        if (isUserInvited) {
            return;
        }
        boolean isOwner = Objects.equals(build.getUser().getId(), userInfo.getUserId());
        if (isOwner) {
            return;
        }
        throw new NoAccessException("Access denied: invalid token or user is not invited.");
    }

    private long calculateHP(int level) {
        double hp;
        if (level >= 1 && level <= 25) {
            hp = 300 + 500 * Math.pow(((level - 1) / 24.0), 1.5);
        } else if (level >= 26 && level <= 40) {
            hp = 800 + 650 * Math.pow(((level - 25) / 15.0), 1.1);
        } else if (level >= 41 && level <= 60) {
            hp = 1450 + 450 * (1 - Math.pow(1 - ((level - 40) / 20.0), 1.2));
        } else if (level >= 61 && level <= 99) {
            hp = 1900 + 200 * (1 - Math.pow(1 - ((level - 60) / 39.0), 1.2));
        } else {
            throw new EntityBadRequestException("Vigor level can`t be more than 99");
        }

        return (long) Math.floor(hp);
    }

    private long calculateFP(int level) {
        double hp;
        if (level >= 1 && level <= 15) {
            hp = 50 + 45 * ((level - 1) / 14.0);
        } else if (level >= 16 && level <= 35) {
            hp = 95 + 105 * ((level - 15) / 20.0);
        } else if (level >= 36 && level <= 60) {
            hp = 200 + 150 * (1 - Math.pow(1 - ((level - 35) / 25.0), 1.2));
        } else if (level >= 61 && level <= 99) {
            hp = 350 + 100 * ((level - 60) / 39.0);
        } else {
            throw new EntityBadRequestException("Intelligence level can`t be more than 99");
        }

        return (long) Math.floor(hp);
    }

    private long calculateStamina(int level) {
        double hp;
        if (level >= 1 && level <= 15) {
            hp = 80 + 25 * ((level - 1) / 14.0);
        } else if (level >= 16 && level <= 35) {
            hp = 105 + 25 * ((level - 15) / 15.0);
        } else if (level >= 36 && level <= 60) {
            hp = 130 + 25 * ((level - 30) / 20.0);
        } else if (level >= 61 && level <= 99) {
            hp = 155 + 15 * ((level - 50) / 49.0);
        } else {
            throw new EntityBadRequestException("Endurance level can`t be more than 99");
        }

        return (long) Math.floor(hp);
    }

    private int getTotalAttributesSum(Integer vigor, Integer mind, Integer endurance, Integer strength,
                                      Integer dexterity, Integer intelligence, Integer faith, Integer arcana) {
        return (vigor == null ? 0 : vigor)
                + (mind == null ? 0 : mind)
                + (endurance == null ? 0 : endurance)
                + (strength == null ? 0 : strength)
                + (dexterity == null ? 0 : dexterity)
                + (intelligence == null ? 0 : intelligence)
                + (faith == null ? 0 : faith)
                + (arcana == null ? 0 : arcana);
    }

    private List<CharacterDetailedResponseDto> getDetailedCharacters(Build build) {
        List<Character> characters = new ArrayList<>(build.getCharacters());
        List<CharacterDetailedResponseDto> charactersDetailed = new ArrayList<>();
        for (Character c : characters) {
            CharacterDetailedResponseDto detailed = characterMapper.toDetailedDto(c);
            Attribute characterAttribute = c.getAttribute();
            Attribute startClassAttribute = c.getStartClass().getAttribute();
            int level = c.getStartClass().getLevel() +
                    getTotalAttributesSum(
                            characterAttribute.getVigor(),
                            characterAttribute.getEndurance(),
                            characterAttribute.getIntelligence(),
                            characterAttribute.getMind(),
                            characterAttribute.getDexterity(),
                            characterAttribute.getStrength(),
                            characterAttribute.getFaith(),
                            characterAttribute.getArcana()
                    ) -
                    getTotalAttributesSum(
                            startClassAttribute.getVigor(),
                            characterAttribute.getEndurance(),
                            startClassAttribute.getIntelligence(),
                            startClassAttribute.getMind(),
                            startClassAttribute.getDexterity(),
                            startClassAttribute.getStrength(),
                            startClassAttribute.getFaith(),
                            startClassAttribute.getArcana()
                    );
            detailed.setLevel(level);
            detailed.setHp(calculateHP(detailed.getAttribute().getVigor()));
            detailed.setFp(calculateFP(detailed.getAttribute().getMind()));
            detailed.setStamina(calculateStamina(detailed.getAttribute().getEndurance()));
            charactersDetailed.add(detailed);
        }
        return charactersDetailed;
    }

    private void saveBuildInventoryItems(List<Long> itemIds, Build build) {
        List<InventoryItem> items = inventoryItemService.findAllById(itemIds);
        validateInventoryItems(items);
        Set<BuildInventoryItem> buildInventoryItems = new HashSet<>();
        for (InventoryItem item : items) {
            BuildInventoryItem buildInventoryItem = new BuildInventoryItem();
            buildInventoryItem.setBuild(build);
            buildInventoryItem.setInventoryItem(item);
            buildInventoryItems.add(buildInventoryItem);
        }
        buildInventoryItemRepository.saveAll(buildInventoryItems);
        build.setBuildInventoryItems(buildInventoryItems);
    }

    private void validateInventoryItems(List<InventoryItem> items) {
        Map<ItemType, Integer> itemTypeCountMap = new EnumMap<>(ItemType.class);

        for (InventoryItem dto : items) {
            ItemType itemType = dto.getItemType();
            itemTypeCountMap.put(itemType, itemTypeCountMap.getOrDefault(itemType, 0) + 1);
        }

        itemTypeCountMap.entrySet().stream()
                .filter(entry -> {
                    ItemType itemType = entry.getKey();
                    int count = entry.getValue();
                    return (itemType == ItemType.HEAD && count > 1)
                            || (itemType == ItemType.CHEST && count > 1)
                            || (itemType == ItemType.HANDS && count > 1)
                            || (itemType == ItemType.LEGS && count > 1)
                            || (itemType == ItemType.RING && count > 4)
                            || (itemType == ItemType.SHIELD && count > 1);
                })
                .forEach(entry -> {
                    ItemType itemType = entry.getKey();
                    String itemTypeName = itemType.name().toLowerCase();
                    throw new EntityConflictException("Элемента типа " + itemTypeName + " не может быть больше " + (itemType == ItemType.RING ? 4 : 1));
                });
    }

    private Build saveBuild(Build build, List<AshesOfWarWeaponCreateRequestDto> ashesOfWarWeaponCreateRequestDtos) {
        build = buildRepository.save(build);
        log.info("Build created successfully with id: {}", build.getId());
        List<AshesOfWarWeapon> ashesOfWarWeaponList = createAshesOfWarWeapons(ashesOfWarWeaponCreateRequestDtos);
        createBuildWeapons(build, ashesOfWarWeaponList);
        return build;
    }

    private List<AshesOfWarWeapon> createAshesOfWarWeapons(List<AshesOfWarWeaponCreateRequestDto> ashesOfWarWeaponCreateRequestDtos) {
        List<AshesOfWarWeapon> ashesOfWarWeaponList = new ArrayList<>();
        for (AshesOfWarWeaponCreateRequestDto requestDto : ashesOfWarWeaponCreateRequestDtos) {
            log.info("Creating new AshesOfWarWeapon with request: {}", requestDto);
            AshesOfWarWeapon ashesOfWarWeapon = createAshesOfWarWeapon(requestDto);
            ashesOfWarWeaponList.add(ashesOfWarWeapon);
            log.info("AshesOfWarWeapon created successfully with id: {}", ashesOfWarWeapon.getId());
        }
        return ashesOfWarWeaponList;
    }

    private AshesOfWarWeapon createAshesOfWarWeapon(AshesOfWarWeaponCreateRequestDto requestDto) {
        AshesOfWarWeapon ashesOfWarWeapon = new AshesOfWarWeapon();
        ashesOfWarWeapon.setWeapon(weaponService.getWeaponOrThrow(requestDto.getWeaponId()));
        ashesOfWarWeapon.setAshesOfWar(ashesOfWarService.getAshOfWarOrThrow(requestDto.getAshesOfWarId()));
        return ashesOfWarWeaponRepository.save(ashesOfWarWeapon);
    }

    private void createBuildWeapons(Build build, List<AshesOfWarWeapon> ashesOfWarWeaponList) {
        Set<BuildWeapon> buildWeapons = new HashSet<>();
        for (AshesOfWarWeapon ashesOfWarWeapon : ashesOfWarWeaponList) {
            BuildWeapon buildWeapon = new BuildWeapon();
            buildWeapon.setBuild(build);
            buildWeapon.setAshesOfWarWeapon(ashesOfWarWeapon);
            log.info("Build created successfully with id: {}", build.getId());
            buildWeapons.add(buildWeapon);
        }
        build.setBuildWeapons(buildWeapons);
        buildWeaponRepository.saveAll(buildWeapons);
    }
}
