package ru.zmaev.domain.mapper;

import org.mapstruct.*;
import ru.zmaev.domain.dto.request.InvitationCreateRequestDto;
import ru.zmaev.domain.dto.response.InvitationResponseDto;
import ru.zmaev.domain.entity.Invitation;
import ru.zmaev.domain.entity.InvitationPrincipal;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface InvitationMapper extends EntityMapper<Invitation, InvitationCreateRequestDto, InvitationResponseDto> {
    @Override
    @Mapping(target = "token", ignore = true)
    Invitation toEntity(InvitationCreateRequestDto requestDto);

    @Override
    @Mapping(target = "userIds", source = "invitedUsers", qualifiedByName = "mapInvitedUsers")
    @Mapping(target = "buildId", source = "build.id")
    InvitationResponseDto toDto(Invitation entity);

    @AfterMapping
    default void setToken(@MappingTarget Invitation invitation) {
        if (invitation.getToken() == null) {
            invitation.setToken(UUID.randomUUID().toString());
        }
    }

    @Named("mapInvitedUsers")
    default List<String> mapInvitedUsers(List<InvitationPrincipal> invitedUsers) {
        return invitedUsers != null ? invitedUsers.stream()
                .map(it -> it.getInvitedUser().getId())
                .toList() : Collections.emptyList();
    }
}
